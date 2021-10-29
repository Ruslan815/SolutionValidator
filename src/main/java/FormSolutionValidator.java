import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class FormSolutionValidator extends JFrame implements ActionListener {

    JLabel labelTaskNumber, labelTaskText, labelTestResult;
    JComboBox<String> comboBoxTaskNumber;
    JButton buttonBaseCheck, buttonRealtimeCheck;
    JTextArea textAreaTaskText, textAreaTestResult;

    FormSolutionValidator() {
        labelTaskNumber = new JLabel("Задание №");
        labelTaskNumber.setBounds(10, 12, 90, 15);
        this.add(labelTaskNumber);

        comboBoxTaskNumber = new JComboBox<>();
        comboBoxTaskNumber.setBounds(85, 10, 180, 20);
        comboBoxTaskNumber.setBackground(new Color(0x7373E7));
        comboBoxTaskNumber.addItem("1");
        comboBoxTaskNumber.addItem("2");
        comboBoxTaskNumber.addItem("3");
        this.add(comboBoxTaskNumber);

        buttonBaseCheck = new JButton("Базовая проверка");
        buttonBaseCheck.setBounds(320, 8, 180, 25);
        buttonBaseCheck.addActionListener(this);
        this.add(buttonBaseCheck);

        buttonRealtimeCheck = new JButton("Проверка в реальном времени");
        buttonRealtimeCheck.setBounds(300, 40, 220, 25);
        buttonRealtimeCheck.addActionListener(this);
        this.add(buttonRealtimeCheck);

        labelTaskText = new JLabel("Задание:");
        labelTaskText.setBounds(10, 70, 90, 15);
        this.add(labelTaskText);

        textAreaTaskText = new JTextArea();
        textAreaTaskText.setBounds(10, 90, 500, 150);
        this.add(textAreaTaskText);

        labelTestResult = new JLabel("Результаты тестирования:");
        labelTestResult.setBounds(10, 260, 250, 15);
        this.add(labelTestResult);

        textAreaTestResult = new JTextArea();
        textAreaTestResult.setBounds(10, 280, 500, 250);
        this.add(textAreaTestResult);

        this.setTitle("Solution Validator");
        this.setBounds(700, 250, 550, 600);
        this.setResizable(false);
        this.setLayout(null); // Using no layout manager
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == buttonBaseCheck) {
            textAreaTaskText.setText("Base");
            textAreaTestResult.setText("");
        } else if (event.getSource() == buttonRealtimeCheck) {
            textAreaTestResult.setText("Realtime");
            textAreaTaskText.setText("");
        }
    }

    public static void main(String[] args) {
        new FormSolutionValidator();
    }
}