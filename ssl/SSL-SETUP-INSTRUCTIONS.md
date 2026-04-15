# SSL/TLS Setup Instructions
## HRM RMI Leave Management System

This guide will help you set up SSL/TLS encryption for secure RMI communication between the server and clients.

---

## Manual Setup (Step by Step)

### Prerequisites
- Java JDK installed (includes `keytool` command)
- Command prompt/terminal access

### Step 1: Navigate to SSL Directory
```bash
cd ssl
```

### Step 2: Generate Server Keystore
This creates the server's private key and certificate.

**If keytool is in your PATH:**
```bash
keytool -genkeypair -alias serverkey -keyalg RSA -keysize 2048 -keystore serverkeystore.jks -validity 365 -storepass password -keypass password -dname "CN=localhost, OU=Crest Solutions, O=HRM System, L=Kuala Lumpur, ST=WP, C=MY"
```

**If keytool is NOT in your PATH (Windows):**
```bash
"C:\Program Files\Java\jdk-17\bin\keytool" -genkeypair -alias serverkey -keyalg RSA -keysize 2048 -keystore serverkeystore.jks -validity 365 -storepass password -keypass password -dname "CN=localhost, OU=Crest Solutions, O=HRM System, L=Kuala Lumpur, ST=WP, C=MY"
```
*Replace `jdk-17` with your actual JDK version*

**Using IntelliJ Terminal (Recommended):**
IntelliJ IDEA's built-in terminal has Java in its PATH automatically. Just open the terminal in IntelliJ and run the commands directly.

**What this does:**
- Creates a keystore file: `serverkeystore.jks`
- Generates an RSA key pair (2048-bit)
- Valid for 365 days
- Password: `password` (can be changed, but update code accordingly)

### Step 3: Export Server Certificate
This exports the public certificate that clients will trust.

```bash
keytool -export -alias serverkey -keystore serverkeystore.jks -file server.cer -storepass password
```

**What this does:**
- Exports the server's certificate to `server.cer`
- This file can be distributed to clients

### Step 4: Create Client Truststore
This imports the server certificate into the client's truststore.

```bash
keytool -import -alias servercert -file server.cer -keystore clienttruststore.jks -storepass password -noprompt
```

**What this does:**
- Creates a truststore: `clienttruststore.jks`
- Imports the server certificate
- Clients use this to verify the server's identity

---

## Generated Files

After setup, you should have these files in the `ssl/` directory:

| File | Description | Used By |
|------|-------------|---------|
| `serverkeystore.jks` | Server's private key and certificate | Server |
| `server.cer` | Server's public certificate | For distribution |
| `clienttruststore.jks` | Client's trusted certificates | Client |

**⚠️ IMPORTANT:** Keep `serverkeystore.jks` secure! It contains the server's private key.

---

## Running the System with SSL/TLS

### 1. Start the Server
```bash
# From project root
java -cp target/classes server.HRMServer
```

You should see:
```
========================================
   HRM RMI Server - Crest Solutions    
========================================
[SECURITY] Initializing SSL/TLS...
[SECURITY] SSL/TLS enabled successfully
[SERVER] HRM RMI Server is running on port 1099...
[SERVER] Waiting for secure client connections...
```

### 2. Start the Client
```bash
# From project root
java -cp target/classes client.LoginFrame
```

You should see:
```
[SECURITY] Initializing client SSL/TLS...
[SSL] Client SSL/TLS configured successfully
[SSL] Using truststore: ssl/clienttruststore.jks
[CLIENT] Connected to secure RMI server
```

---

## Multi-Computer Setup

### Computer A (Server)
1. Run the SSL setup script on the server computer
2. Start the HRM server application
3. Note the server's IP address:
   - Windows: `ipconfig`
   - Linux/Mac: `ifconfig` or `ip addr`

### Computer B (Client)
1. Copy `clienttruststore.jks` from server to client computer
2. Place it in the `ssl/` directory of the client application
3. Update the server address in `LoginFrame.java`:
   ```java
   service = (HRMService) Naming.lookup("rmi://SERVER_IP:1099/HRMService");
   ```
   Replace `localhost` with the actual server IP address
4. Recompile and run the client

---

## How SSL/TLS Works in This System

1. **Server Startup:**
   - Loads `serverkeystore.jks`
   - Creates SSL-enabled RMI registry on port 1099
   - Exports RMI service with SSL socket factories

2. **Client Connection:**
   - Loads `clienttruststore.jks`
   - Looks up RMI service over SSL
   - Performs TLS handshake

3. **TLS Handshake:**
   - Client connects to server
   - Server presents its certificate
   - Client verifies certificate against truststore
   - If valid, encrypted session is established
   - 🔒 All RMI communication is now encrypted

---

## Troubleshooting

### Error: "Failed to create SSL server socket"
- Check that `serverkeystore.jks` exists in the `ssl/` directory
- Verify the keystore password matches in the code

### Error: "Failed to connect to RMI server"
- Ensure the server is running
- Check that `clienttruststore.jks` exists in the `ssl/` directory
- Verify the server address is correct (use IP address for remote connections)

### Error: "Certificate verification failed"
- The client truststore may not contain the server's certificate
- Re-run the SSL setup to regenerate certificates

### Port Already in Use
- Another application may be using port 1099
- Stop the other application or change the RMI port

---

## Security Notes

### For Development:
- Default password is `password` (insecure, but fine for learning)
- Certificates are self-signed and valid for 365 days

### For Production:
1. Use strong passwords:
   ```bash
   keytool -genkeypair ... -storepass YOUR_STRONG_PASSWORD -keypass YOUR_STRONG_PASSWORD
   ```
   Then update passwords in:
   - `RMISSLServerSocketFactory.java`
   - `RMISSLClientSocketFactory.java`
   - `SSLClientConfig.java`

2. Use proper certificates from a Certificate Authority (CA)

3. Don't commit keystore files to version control
   - Add to `.gitignore`:
     ```
     ssl/*.jks
     ssl/*.cer
     ```

4. Increase certificate validity for production:
   ```bash
   -validity 3650  # 10 years
   ```

5. Use stronger key size:
   ```bash
   -keysize 4096
   ```

---

## Testing SSL/TLS

To verify SSL/TLS is working:

1. Start the server - look for: `[SECURITY] SSL/TLS enabled successfully`
2. Start the client - look for: `[SSL] Client SSL/TLS configured successfully`
3. Try to login - if successful, SSL/TLS is working!

You can also use network monitoring tools like Wireshark to verify that the traffic is encrypted (it will show as TLS traffic, not plain text).

---

## Comparison with Lecturer's Sample Code

| Lecturer's Example | This Implementation |
|-------------------|---------------------|
| Simple TLS client/server | Full RMI with SSL/TLS |
| Direct socket communication | RMI remote method invocation |
| Manual message exchange | Automatic serialization/deserialization |
| Single connection | Multi-threaded server (RMI handles this) |

**Key Enhancement:** This implementation integrates SSL/TLS into your existing RMI application using custom socket factories, maintaining all RMI functionality while adding encryption.

---

## Additional Commands

### View Keystore Contents
```bash
keytool -list -v -keystore serverkeystore.jks -storepass password
```

### View Truststore Contents
```bash
keytool -list -v -keystore clienttruststore.jks -storepass password
```

### Delete and Recreate (if needed)
```bash
del serverkeystore.jks server.cer clienttruststore.jks
# Then run the three keytool commands again
```

---

## Support

If you encounter issues:
1. Check the console output for error messages
2. Verify all files are in the correct locations
3. Ensure Java is properly installed (`java -version`)
4. Make sure port 1099 is not blocked by firewall

For questions, contact your instructor or TA.
