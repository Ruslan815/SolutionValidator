import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FormSolutionValidator extends JFrame implements ActionListener {

    private static final String SECRET_PATH_TO_EXECUTABLE_FILES = "R:\\"; // TODO + secret path

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