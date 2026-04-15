package server;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;
import java.security.KeyStore;

/**
 * Custom SSL Server Socket Factory for RMI
 */
public class RMISSLServerSocketFactory implements RMIServerSocketFactory, Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final String KEYSTORE_PATH = "ssl/serverkeystore.jks";
    private static final String KEYSTORE_PASSWORD = "password";
    
    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        try {
            // Load keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(KEYSTORE_PATH)) {
                keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
            }
            
            // Initialize key manager
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());
            
            // Initialize SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);
            
            // Create SSL server socket
            SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
            
            // Enable specific cipher suites and protocols
            serverSocket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});
            
            return serverSocket;
        } catch (Exception e) {
            throw new IOException("Failed to create SSL server socket", e);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof RMISSLServerSocketFactory;
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
