import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Validator {

    private static final String DATA_DELIMITER = "~";
    //new ProcessBuilder(new String[]{"cmd.exe", "/c", "R:\\task1.exe"}).start();
    // System.out.println(System.getProperty("user.dir"));
    //File dir = new File("D:\\bat_scripts");
    //processBuilder.directory(dir);

    public static void validateSolution(String referenceFilename/*taskNumber*/, String solutionFilename, String[] inputData, boolean isStandardInput) {
        String referenceCommand; // taskNumber instead refFilename ??? TODO
        String solutionCommand;
        if (isStandardInput) { // From keyboard
            referenceCommand = "exec\\" + referenceFilename;
            solutionCommand = solutionFilename;
        } else { // From passed command line params
            String passedParams = Arrays.toString(inputData)
                    .replace("[", "").replace("]", "").replace(",", "");
            referenceCommand = "exec\\" + referenceFilename + " " + passedParams;
            solutionCommand = solutionFilename + " " + passedParams;
        }
        System.out.println("Ref command to execute: " + referenceCommand);
        System.out.println("Sol command to execute: " + solutionCommand);

        List<String> referenceOutput = startProcess(referenceCommand, inputData, isStandardInput);
        List<String> solutionOutput = startProcess(solutionCommand, inputData, isStandardInput);

        if (referenceOutput.equals(solutionOutput)) {
            System.out.println("Test passed.");
        } else {
            System.err.println("Test failed.\nInput data: " + Arrays.toString(inputData)
                    + "\nReference output:\n" + referenceOutput + "\nYour output:\n" + solutionOutput);
        }
    }

    private static List<String> startProcess(String executeCommand, String[] inputData, boolean isStandardInput) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", executeCommand); // "exec\\task1.exe"
        List<String> resultList = new ArrayList<>();

        try {
            Process process = processBuilder.start();

            if (isStandardInput) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                for (String someInput : inputData) {
                    writer.write(someInput + "\n");
                }
                writer.flush(); // writer.close();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                resultList.add(line);
                //System.out.println(line);
            }
            reader.close();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new ValidatorException("Error while file executing.\n" +
                        "Error code:" + exitCode);
            }
            if (process.getErrorStream().read() != -1) {
                throw new ValidatorException("Error while file executing.\n" +
                        "Error stream:\n" + process.getErrorStream());
            }
            //System.out.println("\nExited with code : " + exitCode); // process.exitValue()
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static void testSolutionOnTestCases(String solutionFilename, boolean isStandardInput, String testFilename) {
        String[] readFile = readTestCasesFromFile(testFilename); // чётное - Input, нечётное - Output
        StringBuilder sb = new StringBuilder();
        //***************
        for (int i = 0; i < readFile.length / 2; i++) {
            //System.out.println("Test#" + i);
            //System.out.println("Input: " + readFile[i * 2]);
            //System.out.println("Output: " + readFile[i * 2 + 1]);
            //***************
            String[] inputData = readFile[i * 2].split(" ");
            String executeCommand;
            if (isStandardInput) { // From keyboard
                executeCommand = solutionFilename;
            } else { // From passed command line params
                String passedParams = Arrays.toString(inputData)
                        .replace("[", "").replace("]", "").replace(",", "");
                executeCommand = solutionFilename + " " + passedParams;
            }
            //***************

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", executeCommand);
            StringBuilder resultSB = new StringBuilder();

            try {
                Process process = processBuilder.start();

                if (isStandardInput) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                    for (String someInput : inputData) {
                        writer.write(someInput + "\n");
                    }
                    writer.flush();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    resultSB.append(line);
                    //System.out.println(line);
                }
                reader.close();

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new ValidatorException("Error while file executing.\n" +
                            "Error code:" + exitCode);
                }
                if (process.getErrorStream().read() != -1) {
                    throw new ValidatorException("Error while file executing.\n" +
                            "Error stream:\n" + process.getErrorStream());
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            if (resultSB.toString().equals(readFile[i * 2 + 1])) {
                System.out.println("Test#" + i + " PASSED.");
                sb.append("Test#").append(i).append(" PASSED.");
            } else {
                System.out.println("Test#" + i + " FAILED.");
                sb.append("Test#").append(i).append(" FAILED.");
            }
            sb.append(System.lineSeparator());
        }
        //***************
        Main.form.textAreaTestResult.setText(sb.toString());
    }

    private static String[] readTestCasesFromFile(String filename) {
        InputStream in = Validator.class.getResourceAsStream(filename);
        if (in == null) {
            System.out.println("NULL!");
            throw new ValidatorException("Cannot open testCasesFile: " + filename);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        StringBuilder stringBuilder = new StringBuilder();
        try {
            String someString;
            while ((someString = reader.readLine()) != null) {
                stringBuilder.append(someString);
            }
        } catch (Exception e) {
            System.err.println("Cannot read testCasesFile!");
            e.printStackTrace();
        }

        return stringBuilder.toString().split(Validator.DATA_DELIMITER); // чётное - Input, нечётное - Output
    }

    public static void main(String[] args) {
        Validator.validateSolution("task1.exe", "R:\\TestProg.exe", new String[]{"2"}, true);
        Validator.testSolutionOnTestCases("R:\\TestProg.exe", true, "task1.txt");
        Validator.testSolutionOnTestCases("R:\\TestProg.exe", true, "task2.txt");
    }
}
