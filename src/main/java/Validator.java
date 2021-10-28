import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Validator {

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
            System.out.println("Test failed.");
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
                        "Error code:\n" + exitCode);
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

    public static void main(String[] args) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (!isWindows) {
            System.out.println("This software can be run only on Windows.");
            System.exit(777);
        }

        Validator.validateSolution("task1.exe", "R:\\TestProg.exe", new String[]{"2"}, true);
    }
}
