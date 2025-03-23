
package controller;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Student;

public class StudentImplement extends UnicastRemoteObject implements StudentInterface {
    private static final long serialVersionUID = 1L;
    private Connection conn;

    public StudentImplement() throws RemoteException {
        super();
        connectToDb();
    }

    @Override
    public void connectToDb() throws RemoteException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/rmi";
            String userName = "root";
            String password = "123456789";
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Failed to connect to the database.");
        }
    }

    @Override
    public boolean addStudent(Student student) throws RemoteException {
        if (conn == null) {
            throw new RemoteException("Connection to database is not established. Please connect to the database first.");
        }

        String sql = "INSERT INTO tblstudent(id, name, age, id_faculty) VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, student.getId());
            ps.setString(2, student.getName());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getIdFaculty());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student added successfully.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to add student to the database.");
        }
        return false;
    }

    @Override
    public boolean removeStudent(int id) throws RemoteException {
        if (conn == null) {
            throw new RemoteException("Connection to database is not established. Please connect to the database first.");
        }

        String sql = "DELETE FROM tblstudent WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student removed successfully.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to remove student from the database.");
        }
        return false;
    }

    @Override
    public Student findStudent(int id) throws RemoteException {
        if (conn == null) {
            throw new RemoteException("Connection to database is not established. Please connect to the database first.");
        }

        String sql = "SELECT * FROM tblstudent WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int studentId = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String id_faculty = rs.getString("id_faculty");

                return new Student(studentId, name, age, id_faculty);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to find student in the database.");
        }
        System.out.println("Student not found.");
        return null;
    }

    @Override
    public boolean updateStudentName(int id, String newName) throws RemoteException {
        if (conn == null) {
            throw new RemoteException("Connection to database is not established. Please connect to the database first.");
        }

        String sql = "UPDATE tblstudent SET name = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newName);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student name updated successfully.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to update student name in the database.");
        }
        return false;
    }

    @Override
    public boolean updateStudentAge(int id, int newAge) throws RemoteException {
        if (conn == null) {
            throw new RemoteException("Connection to database is not established. Please connect to the database first.");
        }

        String sql = "UPDATE tblstudent SET age = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, newAge);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student age updated successfully.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to update student age in the database.");
        }
        return false;
    }

    @Override
    public ArrayList<Student> getStudentsByFaculty(String facultyId) throws RemoteException {
        if (conn == null) {
            throw new RemoteException("Connection to database is not established. Please connect to the database first.");
        }

        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM tblstudent WHERE id_faculty = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, facultyId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String id_faculty = rs.getString("id_faculty");

                Student student = new Student(id, name, age, id_faculty);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to get students by faculty from the database.");
        }
        return students;
    }

    @Override
    public boolean updateStudentFaculty(int id, String newFaculty) throws RemoteException {
        if (conn == null) {
            throw new RemoteException("Connection to database is not established. Please connect to the database first.");
        }

        String sql = "UPDATE tblstudent SET id_faculty = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newFaculty);
            ps.setInt(2, id);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student faculty updated successfully.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to update student faculty in the database.");
        }
        return false;
    }

    @Override
    public ArrayList<Student> selectAll() throws RemoteException {
        if (conn == null) {
            throw new RemoteException("Connection to database is not established. Please connect to the database first.");
        }

        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM tblstudent";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String id_faculty = rs.getString("id_faculty");

                Student student = new Student(id, name, age, id_faculty);
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to select all students from the database.");
        }
        return students;
    }
    @Override
    public boolean isFacultyExists(String facultyId) throws RemoteException {
        if (conn == null) {
            throw new RemoteException("Connection to database is not established. Please connect to the database first.");
        }

        String sql = "SELECT id_faculty FROM tblfaculty WHERE id_faculty = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, facultyId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Trả về true nếu tồn tại, ngược lại trả về false
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RemoteException("Failed to check faculty existence in the database.");
        }
    }


}
