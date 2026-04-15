package server;

import client.RMISSLClientSocketFactory;
import service.HRMServiceImpl;
import remote.HRMService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// RMI server inherently supports multithreading - each client request runs in its own thread
public class HRMServer {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   HRM RMI Server - Crest Solutions    ");
        System.out.println("========================================");

        try {
            // Initialize SSL/TLS for secure RMI communication
            System.out.println("[SECURITY] Initializing SSL/TLS...");
            
            // Create custom SSL socket factories
            RMISSLServerSocketFactory serverSocketFactory = new RMISSLServerSocketFactory();
            RMISSLClientSocketFactory clientSocketFactory = new RMISSLClientSocketFactory();
            
            // Create service with SSL socket factories
            HRMServiceImpl service = new HRMServiceImpl();
            
            // Export the remote object with SSL socket factories
            HRMService stub = (HRMService) UnicastRemoteObject.exportObject(service, 0, 
                clientSocketFactory, serverSocketFactory);
            
            // Create registry with SSL
            Registry registry = LocateRegistry.createRegistry(1099, 
                clientSocketFactory, serverSocketFactory);
            
            registry.rebind("HRMService", stub);

            System.out.println("[SECURITY] SSL/TLS enabled successfully");
            System.out.println("[SERVER] HRM RMI Server is running on port 1099...");
            System.out.println("[SERVER] Waiting for secure client connections...");
        } catch (Exception e) {
            System.err.println("[ERROR] Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
