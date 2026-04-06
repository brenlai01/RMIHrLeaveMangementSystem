package server;

import service.HRMServiceImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import security.SslPropertyUtil;

// RMI server inherently supports multithreading - each client request runs in its own thread
public class HRMServer {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("        HRM RMI Server - JB Tech        ");
        System.out.println("========================================");

        try {
            configureSslProperties();

            RMIClientSocketFactory clientSocketFactory = new SslRMIClientSocketFactory();
            RMIServerSocketFactory serverSocketFactory = new SslRMIServerSocketFactory();

            HRMServiceImpl service = new HRMServiceImpl(clientSocketFactory, serverSocketFactory);
            Registry registry = LocateRegistry.createRegistry(1099, clientSocketFactory, serverSocketFactory);
            registry.rebind("HRMService", service);

            System.out.println("[SERVER] HRM RMI Server is running with SSL/TLS on port 1099...");
            System.out.println("[SERVER] Waiting for client connections...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void configureSslProperties() {
        String keyStorePath = SslPropertyUtil.requireProperty("hrm.ssl.keystore");
        String keyStorePassword = SslPropertyUtil.requireProperty("hrm.ssl.keystore.password");
        String trustStorePath = SslPropertyUtil.requireProperty("hrm.ssl.truststore");
        String trustStorePassword = SslPropertyUtil.requireProperty("hrm.ssl.truststore.password");

        System.setProperty("javax.net.ssl.keyStore", keyStorePath);
        System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
        System.setProperty("javax.net.ssl.trustStore", trustStorePath);
        System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
    }
}
