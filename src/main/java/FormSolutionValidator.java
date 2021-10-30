import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FormSolutionValidator extends JFrame implements ActionListener {

    private static final String SECRET_PATH_TO_EXECUTABLE_FILES = "R:\\"; // TODO + secret path
    private static final String[] taskNumbersList = {
            "1_1_1", "1_1_2", "1_1_3", "1_1_4", "1_1_5", "1_1_6", "1_1_7", "1_1_8",
            "1_2_1", "1_2_2", "1_2_3", "1_2_4", "1_2_5", "1_2_6", "1_2_7", "1_2_8", "1_2_9",
            "2_1_1", "2_1_2", "2_1_3", "2_1_4", "2_1_5", "2_1_6", "2_1_7", "2_1_8", "2_1_9", "2_1_10", "2_1_11", "2_1_12", "2_1_13", "2_1_14",
            "3_1_1", "3_1_2", "3_1_3", "3_1_4", "3_1_5", "3_1_6", "3_1_7", "3_1_8",
            "4_1_1", "4_1_2", "4_1_3", "4_1_4", "4_1_5", "4_1_6", "4_1_7", "4_1_8",
            "4_2_1", "4_2_2",
            "4_3_1", "4_3_2", "4_3_3", "4_3_4", "4_3_5", "4_3_6", "4_3_7", "4_3_8", "4_3_9", "4_3_10", "4_3_11", "4_3_12", "4_3_13", "4_3_14", "4_3_15", "4_3_16", "4_3_17", "4_3_18", "4_3_19",
            "5_1_1", "5_1_2", "5_1_3", "5_1_4", "5_1_5", "5_1_6", "5_1_7", "5_1_8", "5_1_9", "5_1_10", "5_1_11", "5_1_12",
            "5_2_1", "5_2_2", "5_2_3", "5_2_4",
            "5_3_1", "5_3_2", "5_3_3", "5_3_4", "5_3_5", "5_3_6",
            "6_1_1", "6_1_2", "6_1_3", "6_1_4", "6_1_5",
            "7_1_1", "7_1_2", "7_1_3",
            "8_1_1", "8_1_2", "8_1_3", "8_1_4", "8_1_5", "8_1_6", "8_1_7", "8_1_8", "8_1_9", "8_1_10", "8_1_11", "8_1_12",
            "8_2_1", "8_2_2", "8_2_3", "8_2_4", "8_2_5", "8_2_6", "8_2_7", "8_2_8", "8_2_9", "8_2_10",
            "8_3_1", "8_3_2", "8_3_3", "8_3_4", "8_3_5", "8_3_6", "8_3_7",
            "8_4_1", "8_4_2", "8_4_3", "8_4_4",
            "9_1_1", "9_1_2",
            "10_1_1", "10_1_2", "10_1_3", "10_1_4", "10_1_5", "10_1_6", "10_1_7", "10_1_8", "10_1_9", "10_1_10", "10_1_11", "10_1_12", "10_1_13", "10_1_14", "10_1_15", "10_1_16", "10_1_17",
            "11_1_1", "11_1_2", "11_1_3", "11_1_4", "11_1_5",
            "12_1_1", "12_1_2", "12_1_3", "12_1_4",
            "13_1_1", "13_1_2", "13_1_3", "13_1_4", "13_1_5", "13_1_6", "13_1_7",
            "14_1_1", "14_1_2", "14_1_3", "14_1_4"};
            //"15_1_1", "15_1_2", "15_1_3", "15_1_4", "15_1_5", "15_1_6", "15_1_7", "15_1_8", "15_1_9", "15_1_10", "15_1_11", "15_1_12", "15_1_13", "15_1_14"}; Files Tasks

    JLabel labelTaskNumber, labelPathToFile, labelTaskText, labelInputData, labelTestResult;
    JTextField textFieldPath;
    JComboBox<String> comboBoxTaskNumber;
    JButton buttonBaseCheck, buttonRealtimeCheck, buttonPath;
    JTextArea textAreaTaskText, textAreaInputData, textAreaTestResult;
    JScrollPane scrollTask, scrollInput, scrollResult;

    FormSolutionValidator() {
        labelTaskNumber = new JLabel("Задание №");
        labelTaskNumber.setBounds(10, 12, 90, 15);
        this.add(labelTaskNumber);

        comboBoxTaskNumber = new JComboBox<>();
        comboBoxTaskNumber.setBounds(85, 10, 180, 20);
        comboBoxTaskNumber.setBackground(new Color(0x7373E7));
        for (String someTaskNumber : taskNumbersList) {
            comboBoxTaskNumber.addItem(someTaskNumber);
        }
        comboBoxTaskNumber.addActionListener(this);
        this.add(comboBoxTaskNumber);

        buttonBaseCheck = new JButton("Базовая проверка");
        buttonBaseCheck.setBounds(320, 18, 180, 25);
        buttonBaseCheck.addActionListener(this);
        this.add(buttonBaseCheck);

        buttonRealtimeCheck = new JButton("Проверка в реальном времени");
        buttonRealtimeCheck.setBounds(300, 50, 220, 30);
        buttonRealtimeCheck.addActionListener(this);
        this.add(buttonRealtimeCheck);

        labelPathToFile = new JLabel("Путь к программе:");
        labelPathToFile.setBounds(10, 40, 120, 15);
        this.add(labelPathToFile);

        textFieldPath = new JTextField();
        textFieldPath.setBounds(10, 60, 255, 25);
        textFieldPath.addActionListener(this);
        this.add(textFieldPath);

        buttonPath = new JButton("Выбрать файл");
        buttonPath.setBounds(140, 85, 124, 25);
        buttonPath.addActionListener(this);
        this.add(buttonPath);

        labelTaskText = new JLabel("Задание:");
        labelTaskText.setBounds(10, 110, 90, 15);
        this.add(labelTaskText);

        textAreaTaskText = new JTextArea();
        textAreaTaskText.setText("Work Hard!");
        textAreaTaskText.setEditable(false);
        scrollTask = new JScrollPane(textAreaTaskText);
        scrollTask.setBounds(10, 130, 500, 150);
        this.add(scrollTask);

        labelInputData = new JLabel("Входные данные(только для проверки в реальном времени):");
        labelInputData.setBounds(10, 240, 500, 150);
        this.add(labelInputData);

        textAreaInputData = new JTextArea();
        scrollInput = new JScrollPane(textAreaInputData);
        scrollInput.setBounds(10, 330, 500, 150);
        this.add(scrollInput);

        labelTestResult = new JLabel("Результаты тестирования:");
        labelTestResult.setBounds(10, 500, 250, 15);
        this.add(labelTestResult);

        textAreaTestResult = new JTextArea();
        textAreaTestResult.setEditable(false);
        scrollResult = new JScrollPane(textAreaTestResult);
        scrollResult.setBounds(10, 520, 500, 250);
        this.add(scrollResult);

        try {
            URL resource = this.getClass().getResource("SV_icon.png");
            BufferedImage image = null;
            if (resource != null) image = ImageIO.read(resource);
            this.setIconImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setTitle("Solution Validator");
        this.setBounds(700, 150, 550, 850);
        this.setResizable(false);
        this.setLayout(null); // Using no layout manager
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        int selectedIndex = comboBoxTaskNumber.getSelectedIndex();
        if (selectedIndex == -1) return;

        if (event.getSource() == comboBoxTaskNumber) { // TODO issue display with UTF-8 and ASCII
            String taskPageNumber = comboBoxTaskNumber.getItemAt(selectedIndex).split("_")[0];
            String filename = "text_task_" + taskPageNumber + ".txt";
            String textFromFile;
            try {
                textFromFile = readTextFromFile(filename);
            } catch (IOException e) {
                textAreaTaskText.setText("Cannot read text task from file: " + filename); //System.err.println("Cannot read text task from file: " + filename);
                return;
            }
            textAreaTaskText.setText(textFromFile);

        } else if (event.getSource() == buttonBaseCheck) {
            String taskNumber = comboBoxTaskNumber.getItemAt(selectedIndex);
            String testFilename = "task_" + taskNumber + ".txt";
            String solutionFilename = textFieldPath.getText();
            if (!Validator.isFileExists(solutionFilename)) {
                FormSolutionValidator.showMessage(this, "Указанный файл не существует!");
                return;
            }
            Validator.testSolutionOnTestCases(solutionFilename, true, testFilename);

        } else if (event.getSource() == buttonRealtimeCheck) {
            String taskNumber = comboBoxTaskNumber.getItemAt(selectedIndex);
            String referenceFilename = SECRET_PATH_TO_EXECUTABLE_FILES + "task_" + taskNumber + ".exe";
            if (!Validator.isFileExists(referenceFilename)) {
                FormSolutionValidator.showMessage(this, "Эталонный файл не существует!");
                return;
            }
            String solutionFilename = textFieldPath.getText();
            if (!Validator.isFileExists(solutionFilename)) {
                FormSolutionValidator.showMessage(this, "Указанный файл не существует!");
                return;
            }
            String[] inputData = textAreaInputData.getText().split("\n"); // Get input data
            Validator.validateSolution(referenceFilename, solutionFilename, inputData, true);

        } else if (event.getSource() == buttonPath) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String absolutePath = selectedFile.getAbsolutePath();
                if (!absolutePath.endsWith(".exe")) {
                    FormSolutionValidator.showMessage(this, "Выберите исполняемый(.exe) файл!");
                    return;
                }
                textFieldPath.setText(absolutePath); //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        }
    }

    private String readTextFromFile(String filename) throws IOException {
        InputStream in = Validator.class.getResourceAsStream(filename);
        if (in == null) {
            System.out.println("NULL!");
            throw new ValidatorException("Cannot open file: " + filename);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        String someString;
        while ((someString = reader.readLine()) != null) {
            stringBuilder.append(someString);
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }

    public static void showMessage(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }
}