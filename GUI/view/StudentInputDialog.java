package view;

import javax.swing.*;
import java.awt.*;

import model.Student; // Import lớp Student vào đây

public class StudentInputDialog extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtId;
    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtFaculty;
    private JButton btnOk;
    private JButton btnCancel;
    private Student student;

    public StudentInputDialog(JFrame parent, String title, String message) {
        super(parent, title, true);
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("ID:"));
        txtId = new JTextField();
        panel.add(txtId);
        panel.add(new JLabel("Name:"));
        txtName = new JTextField();
        panel.add(txtName);
        panel.add(new JLabel("Age:"));
        txtAge = new JTextField();
        panel.add(txtAge);
        panel.add(new JLabel("Faculty:"));
        txtFaculty = new JTextField();
        panel.add(txtFaculty);

        btnOk = new JButton("OK");
        btnOk.addActionListener(e -> {
            int id = Integer.parseInt(txtId.getText());
            String name = txtName.getText();
            int age = Integer.parseInt(txtAge.getText());
            String faculty = txtFaculty.getText();
            student = new Student(id, name, age, faculty);
            dispose();
        });

        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> {
            student = null;
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnOk);
        buttonPanel.add(btnCancel);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public Student getStudent() {
        return student;
    }
}
