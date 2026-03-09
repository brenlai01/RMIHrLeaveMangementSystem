package server;

import service.HRMServiceImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// RMI server inherently supports multithreading - each client request runs in its own thread
public class HRMServer {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   HRM RMI Server - Crest Solutions    ");
        System.out.println("========================================");

        // TODO: [SECURITY - implement last] Add SSL/TLS using javax.net.ssl for secure RMI communication

        try {
            HRMServiceImpl service = new HRMServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("HRMService", service);

            System.out.println("[SERVER] HRM RMI Server is running on port 1099...");
            System.out.println("[SERVER] Waiting for client connections...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
