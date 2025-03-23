package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.Student;
import controller.StudentInterface;

public class StudentGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnFind;
    private JButton btnUpdate;
    private JButton btnSelectAll;
    private JButton btnStudentsByFaculty;
    private JTable tblStudents;
    private StudentInterface studentInterface;

    public StudentGUI(StudentInterface studentInterface) {
        this.studentInterface = studentInterface;
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(6, 1));

        btnAdd = new JButton("Add Student");
        btnRemove = new JButton("Remove Student");
        btnFind = new JButton("Find Student");
        btnUpdate = new JButton("Update Student");
        btnSelectAll = new JButton("Select All Students");
        btnStudentsByFaculty = new JButton("Students by Faculty");

        btnPanel.add(btnAdd);
        btnPanel.add(btnRemove);
        btnPanel.add(btnFind);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnSelectAll);
        btnPanel.add(btnStudentsByFaculty);

        add(btnPanel, BorderLayout.WEST);

        tblStudents = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblStudents);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);

        btnSelectAll.addActionListener(e -> selectAllStudents());
        btnAdd.addActionListener(e -> addNewStudent());
        btnRemove.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter ID of the student to remove:"));
                removeStudent(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnFind.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter ID of the student to find:"));
                findStudentById(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnUpdate.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter ID of the student to update:"));
                updateStudent(id);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnStudentsByFaculty.addActionListener(e -> showFacultyInputDialog());
    }

    private void displayStudents(ArrayList<Student> students) {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Name");
            model.addColumn("Age");
            model.addColumn("Faculty");

            for (Student student : students) {
                model.addRow(new Object[]{student.getId(), student.getName(), student.getAge(), student.getIdFaculty()});
            }

            tblStudents.setModel(model);
        });
    }

    private void displayFoundStudent(Student student) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Age");
        model.addColumn("Faculty");

        if (student != null) {
            model.addRow(new Object[]{student.getId(), student.getName(), student.getAge(), student.getIdFaculty()});
        }

        tblStudents.setModel(model);
    }
    private Student getNewStudentInfo() {
        try {
            int id;
            boolean validId = false;
            do {
                id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter ID:"));
                if (checkIfIdExists(id)) {
                    JOptionPane.showMessageDialog(this, "Student with ID " + id + " already exists. Please enter a different ID.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    validId = true;
                }
            } while (!validId);

            String name = JOptionPane.showInputDialog(this, "Enter Name:");

            int age = 0; // Khởi tạo giá trị mặc định cho tuổi
            boolean validAge = false;
            do {
                String ageInput = JOptionPane.showInputDialog(this, "Enter Age:");
                try {
                    age = Integer.parseInt(ageInput);
                    if (age < 0 || age > 150) {
                        JOptionPane.showMessageDialog(this, "Please enter a valid age between 0 and 150.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        validAge = true;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } while (!validAge);

            String faculty;
            boolean validFaculty = false;
            do {
                faculty = JOptionPane.showInputDialog(this, "Enter Faculty:");
                if (faculty == null || faculty.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Faculty cannot be empty. Please enter a valid faculty.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!isValidFaculty(faculty)) { // Kiểm tra tính hợp lệ của khoa
                    JOptionPane.showMessageDialog(this, "Faculty " + faculty + " does not exist. Please enter a valid faculty ID.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    validFaculty = true;
                }
            } while (!validFaculty);

            return new Student(id, name, age, faculty);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Phương thức kiểm tra tính hợp lệ của mã khoa
    private boolean isValidFaculty(String facultyId) {
        try {
            return studentInterface.isFacultyExists(facultyId);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
    }



    private void addNewStudent() {
        Student newStudent = getNewStudentInfo();
        if (newStudent == null) {
            return;
        }

        // Kiểm tra xem ID đã tồn tại hay chưa trước khi thêm sinh viên mới
        if (checkIfIdExists(newStudent.getId())) {
            JOptionPane.showMessageDialog(this, "Student with ID " + newStudent.getId() + " already exists. Please enter a different ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra xem mã khoa có tồn tại không trước khi thêm sinh viên mới
        try {
            if (!studentInterface.isFacultyExists(newStudent.getIdFaculty())) {
                JOptionPane.showMessageDialog(this, "Faculty " + newStudent.getIdFaculty() + " does not exist. Please enter a valid faculty ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        try {
            boolean success = studentInterface.addStudent(newStudent);
            if (success) {
                JOptionPane.showMessageDialog(this, "Student added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                selectAllStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private boolean checkIfIdExists(int id) {
        try {
            Student student = studentInterface.findStudent(id);
            return student != null;
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
    }

    private void removeStudent(int id) {
        boolean validId = false;
        while (!validId) {
            try {
                // Kiểm tra xem sinh viên có tồn tại không
                boolean studentExists = checkIfIdExists(id);
                if (!studentExists) {
                    JOptionPane.showMessageDialog(this, "Student with ID " + id + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    String input = JOptionPane.showInputDialog(this, "Enter ID of the student to remove:");
                    if (input == null) {
                        // Nếu người dùng chọn Cancel, thoát khỏi phương thức
                        return;
                    }
                    // Tiếp tục vòng lặp để nhập lại ID
                    id = Integer.parseInt(input);
                } else {
                    validId = true; // Kết thúc vòng lặp nếu ID hợp lệ được tìm thấy
                }

                // Nếu ID hợp lệ, tiếp tục với quá trình xóa sinh viên
                if (validId) {
                    // Thực hiện xóa sinh viên
                    boolean success = studentInterface.removeStudent(id);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Student removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        selectAllStudents(); // Cập nhật lại danh sách sinh viên sau khi xóa
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to remove student.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Remote Exception", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // In stack trace cho mục đích gỡ lỗi
                return;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
                String input = JOptionPane.showInputDialog(this, "Enter ID of the student to remove:");
                if (input == null) {
                    // Nếu người dùng chọn Cancel, thoát khỏi phương thức
                    return;
                }
                // Tiếp tục vòng lặp để nhập lại ID
                id = Integer.parseInt(input);
            }
        }
    }


    private void findStudentById(int id) {
        boolean validId = false;
        while (!validId) {
            try {
                Student student = studentInterface.findStudent(id);
                if (student != null) {
                    JOptionPane.showMessageDialog(this, "Student found with ID " + id + ".", "Student Found", JOptionPane.INFORMATION_MESSAGE);
                    displayFoundStudent(student);
                    validId = true; // Kết thúc vòng lặp khi ID hợp lệ được tìm thấy
                } else {
                    JOptionPane.showMessageDialog(this, "Student with ID " + id + " not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    String input = JOptionPane.showInputDialog(this, "Enter ID of the student to find:");
                    if (input == null) {
                        // Nếu người dùng chọn Cancel, thoát khỏi phương thức
                        return;
                    }
                    // Tiếp tục vòng lặp để nhập lại ID
                    id = Integer.parseInt(input);
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                return;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
                String input = JOptionPane.showInputDialog(this, "Enter ID of the student to find:");
                if (input == null) {
                    // Nếu người dùng chọn Cancel, thoát khỏi phương thức
                    return;
                }
                // Tiếp tục vòng lặp để nhập lại ID
                id = Integer.parseInt(input);
            }
        }
    }


    private void selectAllStudents() {
        try {
            ArrayList<Student> students = studentInterface.selectAll();
            displayStudents(students);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateStudent(int id) {
        boolean validId = false;
        while (!validId) {
            try {
                // Lấy thông tin sinh viên từ server
                Student student = studentInterface.findStudent(id);

                // Kiểm tra nếu sinh viên không tồn tại
                if (student == null) {
                    JOptionPane.showMessageDialog(this, "Student with ID " + id + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    String input = JOptionPane.showInputDialog(this, "Enter ID of the student to update:");
                    if (input == null) {
                        // Nếu người dùng chọn Cancel, thoát khỏi phương thức
                        return;
                    }
                    // Tiếp tục vòng lặp để nhập lại ID
                    id = Integer.parseInt(input);
                } else {
                    validId = true; // Kết thúc vòng lặp nếu ID hợp lệ được tìm thấy
                }

                // Nếu ID hợp lệ, tiếp tục với quá trình cập nhật thông tin sinh viên
                if (validId) {
                    // Hiển thị ba nút sửa tên, sửa tuổi và sửa khoa
                    String[] options = {"Update Name", "Update Age", "Update Faculty"};
                    int choice = JOptionPane.showOptionDialog(this, "Select information to update:", "Update Student Information", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                    // Nếu người dùng hủy bỏ hoặc đóng cửa sổ, thoát khỏi phương thức
                    if (choice == JOptionPane.CLOSED_OPTION) {
                        return;
                    }

                    // Hiển thị hộp thoại để nhập thông tin mới
                    String newValue = "";
                    switch (choice) {
                        case 0: // Update Name
                            newValue = JOptionPane.showInputDialog(this, "Enter new name (leave blank to keep current name):", student.getName());
                            break;
                        case 1: // Update Age
                            newValue = JOptionPane.showInputDialog(this, "Enter new age (leave blank to keep current age):", String.valueOf(student.getAge()));
                            break;
                        case 2: // Update Faculty
                            boolean isValidFacultyId = false;
                            do {
                                // Hiển thị hộp thoại nhập mới mã khoa
                                String newFacultyId = JOptionPane.showInputDialog(this, "Enter new faculty ID (leave blank to keep current faculty):", student.getIdFaculty());

                                // Kiểm tra xem mã khoa mới có tồn tại không
                                if (newFacultyId != null && !newFacultyId.isEmpty() && !studentInterface.isFacultyExists(newFacultyId)) {
                                    JOptionPane.showMessageDialog(this, "Faculty with ID " + newFacultyId + " does not exist. Please enter a valid faculty ID.", "Error", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    // Sử dụng mã khoa mới để cập nhật
                                    newValue = newFacultyId;
                                    isValidFacultyId = true;
                                }
                            } while (!isValidFacultyId);
                            break;
                    }

                    // Kiểm tra xem người dùng đã nhập thông tin mới hay không
                    if (newValue == null) {
                        JOptionPane.showMessageDialog(this, "No changes were made.", "Info", JOptionPane.INFORMATION_MESSAGE);
                        return; // Không thực hiện cập nhật nếu không có thông tin mới được nhập
                    }

                    // Cập nhật thông tin sinh viên
                    boolean success = false;
                    switch (choice) {
                        case 0: // Update Name
                            success = studentInterface.updateStudentName(id, newValue);
                            break;
                        case 1: // Update Age
                            int newAge = newValue.isEmpty() ? student.getAge() : Integer.parseInt(newValue);
                            success = studentInterface.updateStudentAge(id, newAge);
                            break;
                        case 2: // Update Faculty
                            success = studentInterface.updateStudentFaculty(id, newValue);
                            break;
                    }

                    if (success) {
                        JOptionPane.showMessageDialog(this, "Student information updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        selectAllStudents(); // Cập nhật lại danh sách sinh viên sau khi cập nhật
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update student information.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                return;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Error", JOptionPane.ERROR_MESSAGE);
                String input = JOptionPane.showInputDialog(this, "Enter ID of the student to update:");
                if (input == null) {
                    // Nếu người dùng chọn Cancel, thoát khỏi phương thức
                    return;
                }
                // Tiếp tục vòng lặp để nhập lại ID
                id = Integer.parseInt(input);
            }
        }
    }



    private void displayStudentsByFaculty(String facultyId) {
        try {
            ArrayList<Student> students = studentInterface.getStudentsByFaculty(facultyId);
            displayStudents(students);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void showFacultyInputDialog() {
        String facultyId;
        boolean isValidFacultyId = false;
        do {
            facultyId = JOptionPane.showInputDialog(this, "Enter Faculty ID:");
            if (facultyId == null) {
                return;
            }
            try {
                isValidFacultyId = studentInterface.isFacultyExists(facultyId);
                if (!isValidFacultyId) {
                    JOptionPane.showMessageDialog(this, "Faculty " + facultyId + " does not exist. Please enter a valid faculty ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                return;
            }
        } while (!isValidFacultyId);

        displayStudentsByFaculty(facultyId);
    }
}
