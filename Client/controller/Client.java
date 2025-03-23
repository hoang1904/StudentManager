

package controller;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;
import model.Student;
import view.StudentGUI;

@SuppressWarnings("unused")
public class Client {
    private static StudentInterface studentInterface;

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            studentInterface = (StudentInterface) registry.lookup("Student");

            StudentGUI studentGUI = new StudentGUI(studentInterface);
            studentGUI.setVisible(true);

        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
