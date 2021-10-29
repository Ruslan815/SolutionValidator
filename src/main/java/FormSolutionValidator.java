import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class FormSolutionValidator extends JFrame implements ActionListener {

    JLabel labelTaskNumber, labelPathToFile, labelTaskText, labelTestResult;
    JTextField textFieldPath;
    JComboBox<String> comboBoxTaskNumber;
    JButton buttonBaseCheck, buttonRealtimeCheck, buttonPath;
    JTextArea textAreaTaskText, textAreaTestResult;
    JScrollPane scrollTask, scrollResult;

    FormSolutionValidator() {
        labelTaskNumber = new JLabel("Задание №");
        labelTaskNumber.setBounds(10, 12, 90, 15);
        this.add(labelTaskNumber);

        comboBoxTaskNumber = new JComboBox<>();
        comboBoxTaskNumber.setBounds(85, 10, 180, 20);
        comboBoxTaskNumber.setBackground(new Color(0x7373E7));
        String[] taskNumbersList = {"1_1_1", "1_1_2"};
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
        textAreaTaskText.setEditable(false);
        scrollTask = new JScrollPane(textAreaTaskText);
        scrollTask.setBounds(10, 130, 500, 150);
        this.add(scrollTask);

        labelTestResult = new JLabel("Результаты тестирования:");
        labelTestResult.setBounds(10, 300, 250, 15);
        this.add(labelTestResult);

        textAreaTestResult = new JTextArea();
        textAreaTestResult.setEditable(false);
        scrollResult = new JScrollPane(textAreaTestResult);
        scrollResult.setBounds(10, 320, 500, 250);
        this.add(scrollResult);

        this.setTitle("Solution Validator");
        this.setBounds(700, 250, 550, 650);
        this.setResizable(false);
        this.setLayout(null); // Using no layout manager
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        int selectedIndex = comboBoxTaskNumber.getSelectedIndex();
        if (selectedIndex == -1) return;

        if (event.getSource() == comboBoxTaskNumber) {
            String taskPageNumber = comboBoxTaskNumber.getItemAt(selectedIndex).split("_")[0];
            String filename = "text_task_" + taskPageNumber + ".txt";
            String textFromFile = "";
            try {
                textFromFile = readTextFromFile(filename);
            } catch (IOException e) {
                textAreaTaskText.setText("Cannot read text task from file: " + filename);
                //System.err.println("Cannot read text task from file: " + filename);
                return;
            }
            textAreaTaskText.setText(textFromFile);
        } else if (event.getSource() == buttonBaseCheck) {
            String taskNumber = comboBoxTaskNumber.getItemAt(selectedIndex);
            String testFilename = "task_" + taskNumber + ".txt";
            String solutionFilename = textFieldPath.getText();
            Validator.testSolutionOnTestCases(solutionFilename, true, testFilename);
        } else if (event.getSource() == buttonRealtimeCheck) {
            String taskNumber = comboBoxTaskNumber.getItemAt(selectedIndex);
            String referenceFilename = "task_" + taskNumber + ".exe";
            String solutionFilename = textFieldPath.getText();
            Validator.validateSolution(referenceFilename, solutionFilename, null, true); // TODO Input data
        } else if (event.getSource() == buttonPath) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                //System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                textFieldPath.setText(selectedFile.getAbsolutePath());
            }
        }
    }

    private String readTextFromFile(String filename) throws IOException {
        InputStream in = Validator.class.getResourceAsStream(filename);
        if (in == null) {
            System.out.println("NULL!");
            throw new ValidatorException("Cannot open file: " + filename);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();
        String someString;
        while ((someString = reader.readLine()) != null) {
            stringBuilder.append(someString);
            stringBuilder.append(System.lineSeparator());
        }

        return stringBuilder.toString();
    }
}