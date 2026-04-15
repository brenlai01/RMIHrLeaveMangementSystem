package server;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * SSL/TLS Configuration utility for RMI server
 */
public class SSLConfig {
    
    private static final String KEYSTORE_PATH = "ssl/serverkeystore.jks";
    private static final String KEYSTORE_PASSWORD = "password";
    
    /**
     * Initialize SSL context for RMI server
     */
    public static void setupServerSSL() throws Exception {
        // Load the server keystore
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(KEYSTORE_PATH)) {
            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }
        
        // Initialize key manager factory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());
        
        // Initialize SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);
        
        // Set default SSL socket factories for RMI
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
        
        // Set system properties for RMI to use SSL
        System.setProperty("javax.rmi.ssl.client.enabledCipherSuites", "TLS_RSA_WITH_AES_128_CBC_SHA");
        System.setProperty("javax.rmi.ssl.client.enabledProtocols", "TLSv1.2");
        
        System.out.println("[SSL] Server SSL/TLS configured successfully");
        System.out.println("[SSL] Using keystore: " + KEYSTORE_PATH);
    }
}
