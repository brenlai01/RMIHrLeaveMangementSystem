package client;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * SSL/TLS Configuration utility for RMI client
 */
public class SSLClientConfig {
    
    private static final String TRUSTSTORE_PATH = "ssl/clienttruststore.jks";
    private static final String TRUSTSTORE_PASSWORD = "password";
    
    /**
     * Initialize SSL context for RMI client
     */
    public static void setupClientSSL() throws Exception {
        // Load the client truststore
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(TRUSTSTORE_PATH)) {
            trustStore.load(fis, TRUSTSTORE_PASSWORD.toCharArray());
        }
        
        // Initialize trust manager factory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(trustStore);
        
        // Initialize SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        
        // Set default SSL socket factory
        SSLContext.setDefault(sslContext);
        
        // Set system properties for RMI to use SSL
        System.setProperty("javax.rmi.ssl.client.enabledCipherSuites", "TLS_RSA_WITH_AES_128_CBC_SHA");
        System.setProperty("javax.rmi.ssl.client.enabledProtocols", "TLSv1.2");
        
        System.out.println("[SSL] Client SSL/TLS configured successfully");
        System.out.println("[SSL] Using truststore: " + TRUSTSTORE_PATH);
    }
}
