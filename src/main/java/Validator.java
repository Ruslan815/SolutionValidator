import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Validator {

    private static final String TEST_DATA_DELIMITER = "~";

    public static void validateSolution(String referenceFilename, String solutionFilename, String[] inputData, boolean isStandardInput) {
        String referenceCommand;
        String solutionCommand;
        referenceCommand = referenceFilename;
        solutionCommand = solutionFilename;

        // Запускаем программы на выполнение
        List<String> referenceOutput = startProcess(referenceCommand, inputData, isStandardInput);
        List<String> solutionOutput = startProcess(solutionCommand, inputData, isStandardInput);

        // Сравниваем вывод полученный от обеих программ
        if (referenceOutput.equals(solutionOutput)) {
            Main.form.textAreaTestResult.setText("Test passed.");
        } else {
            Main.form.textAreaTestResult.setText("Test failed.\nInput data: " + Arrays.toString(inputData)
                    + "\nReference output:\n" + referenceOutput + "\nYour output:\n" + solutionOutput);
        }
    }

    private static List<String> startProcess(String executeCommand, String[] inputData, boolean isStandardInput) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        //processBuilder.command("cmd.exe", "/c", executeCommand); // only Windows
        processBuilder.command("bash", "-c", executeCommand);// only Linux
        List<String> resultList = new ArrayList<>();

        try {
            Process process = processBuilder.start();

            // Подаём данные на вход программе
            if (isStandardInput) {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                for (String someInput : inputData) {
                    writer.write(someInput + "\n");
                }
                writer.flush();
                writer.close();
            }

            // Считываем полученный вывод программы
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                resultList.add(line); //System.out.println(line);
            }
            reader.close();

            // Обрабатываем ошибки
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                FormSolutionValidator.showMessage(null, "Error while file executing.\n" +
                        "Error code:" + exitCode);
                throw new ValidatorException("Error while file executing.\n" + "Error code:" + exitCode);
            }
            if (process.getErrorStream().read() != -1) {
                FormSolutionValidator.showMessage(null, "Error while file executing.\n" +
                        "Error stream:\n" + process.getErrorStream());
                throw new ValidatorException("Error while file executing.\n" + "Error stream:\n" + process.getErrorStream());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static void testSolutionOnTestCases(String solutionFilename, boolean isStandardInput, String testFilename) {
        String[] readFile = readTestCasesFromFile(testFilename); // чётный индекс - Input, нечётный - Output
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < readFile.length / 2; i++) {
            String[] inputData = readFile[i * 2].split(" "); // Все входные данные должны быть на одной строке и разделены пробелом
            String executeCommand;
            if (isStandardInput) { // From keyboard
                executeCommand = solutionFilename;
            } else { // From passed command line params
                String passedParams = Arrays.toString(inputData)
                        .replace("[", "").replace("]", "").replace(",", "");
                executeCommand = solutionFilename + " " + passedParams;
            }

            ProcessBuilder processBuilder = new ProcessBuilder();
            //processBuilder.command("cmd.exe", "/c", executeCommand); // only Windows
            processBuilder.command("bash", "-c", executeCommand);// only Linux
            StringBuilder resultSB = new StringBuilder();

            try {
                Process process = processBuilder.start();

                // Подаём данные на вход программе
                if (isStandardInput) {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                    for (String someInput : inputData) {
                        writer.write(someInput + "\n");
                    }
                    writer.flush();
                    writer.close();
                }

                // Считываем полученный вывод программы
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    resultSB.append(line); //System.out.println(line);
                }
                reader.close();

                // Обрабатываем ошибки
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    FormSolutionValidator.showMessage(null, "Error while file executing.\n" +
                            "Error code:" + exitCode);
                    throw new ValidatorException("Error while file executing.\n" + "Error code:" + exitCode);
                }
                if (process.getErrorStream().read() != -1) {
                    FormSolutionValidator.showMessage(null, "Error while file executing.\n" +
                            "Error stream:\n" + process.getErrorStream());
                    throw new ValidatorException("Error while file executing.\n" + "Error stream:\n" + process.getErrorStream());
                }
            } catch (IOException | InterruptedException e) {
                FormSolutionValidator.showMessage(null, "Exception in class: " + e.getClass().toString());
                e.printStackTrace();
            }

            // Сверяем вывод программы с ожидаемым выводом
            if (resultSB.toString().equals(readFile[i * 2 + 1])) {
                System.out.println("Test#" + i + " PASSED.");
                sb.append("Test#").append(i).append(" PASSED.");
            } else {
                System.out.println("Test#" + i + " FAILED.");
                sb.append("Test#").append(i).append(" FAILED.");
            }
            sb.append(System.lineSeparator());
        }

        Main.form.textAreaTestResult.setText(sb.toString());
    }

    // Считываем текстовые файлы тестов из resources -> taskN
    private static String[] readTestCasesFromFile(String filename) {
        InputStream in = Validator.class.getResourceAsStream(filename);
        if (in == null) {
            FormSolutionValidator.showMessage(null, "Cannot open testCasesFile: " + filename);
            throw new ValidatorException("Cannot open testCasesFile: " + filename);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in)); // Add StandardCharsets.UTF_8 to InputStreamReader arg if be issues with encoding

        StringBuilder stringBuilder = new StringBuilder();
        try {
            String someString;
            while ((someString = reader.readLine()) != null) {
                stringBuilder.append(someString);
            }
        } catch (Exception e) {
            FormSolutionValidator.showMessage(null, "Cannot read testCasesFile!");
            e.printStackTrace();
        }

        return stringBuilder.toString().split(Validator.TEST_DATA_DELIMITER);
    }

    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && !file.isDirectory();
    }
}
