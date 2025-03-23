
package main;

import controller.StudentImplement;
import controller.StudentInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {
    public static void main(String[] args) {
        try {
            // Tạo thanh đăng ký phía server
            Registry registry = LocateRegistry.createRegistry(1099);
            
            // Tạo đối tượng StudentImplement
            StudentInterface studentSkeleton = new StudentImplement();
            
            // Đăng ký đối tượng studentSkeleton với thanh đăng ký
            registry.rebind("Student", studentSkeleton);
            
            System.out.println("Server is running...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
