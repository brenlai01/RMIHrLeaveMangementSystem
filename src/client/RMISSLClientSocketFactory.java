package client;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.security.KeyStore;

/**
 * Custom SSL Client Socket Factory for RMI
 */
public class RMISSLClientSocketFactory implements RMIClientSocketFactory, Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final String TRUSTSTORE_PATH = "ssl/clienttruststore.jks";
    private static final String TRUSTSTORE_PASSWORD = "password";
    
    @Override
    public Socket createSocket(String host, int port) throws IOException {
        try {
            // Load truststore
            KeyStore trustStore = KeyStore.getInstance("JKS");
            try (FileInputStream fis = new FileInputStream(TRUSTSTORE_PATH)) {
                trustStore.load(fis, TRUSTSTORE_PASSWORD.toCharArray());
            }
            
            // Initialize trust manager
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);
            
            // Initialize SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            
            // Create SSL socket
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) ssf.createSocket(host, port);
            
            // Enable specific protocols
            socket.setEnabledProtocols(new String[]{"TLSv1.2", "TLSv1.3"});
            
            return socket;
        } catch (Exception e) {
            throw new IOException("Failed to create SSL client socket", e);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof RMISSLClientSocketFactory;
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
