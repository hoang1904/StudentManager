package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.Student;

public interface StudentInterface extends Remote {
    boolean addStudent(Student student) throws RemoteException;
    boolean removeStudent(int id) throws RemoteException;
    Student findStudent(int id) throws RemoteException;
    ArrayList<Student> selectAll() throws RemoteException;
    ArrayList<Student> getStudentsByFaculty(String facultyId) throws RemoteException;
    boolean updateStudentName(int id, String newName) throws RemoteException;
    boolean updateStudentAge(int id, int newAge) throws RemoteException;
    boolean updateStudentFaculty(int id, String newFaculty) throws RemoteException;
    void connectToDb() throws RemoteException;
    boolean isFacultyExists(String facultyId) throws RemoteException; 
}
