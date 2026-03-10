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
            // MULTITHREADING POINT: registry.rebind() registers the remote service so that the
            // RMI runtime can accept incoming client connections.  From this point onward, the
            // JVM's internal RMI thread pool dispatches every incoming remote method call
            // (e.g. login(), applyLeave()) to HRMServiceImpl on its own dedicated thread —
            // no manual Thread creation is needed.  Each concurrent client therefore runs
            // inside a separate "RMI TCP Connection(n)" thread automatically.
            registry.rebind("HRMService", service);

            System.out.println("[SERVER] HRM RMI Server is running on port 1099...");
            System.out.println("[SERVER] Waiting for client connections...");
            // NOTE: the main thread exits here; the RMI runtime keeps the JVM alive and
            // continues dispatching remote calls on its own threads.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
