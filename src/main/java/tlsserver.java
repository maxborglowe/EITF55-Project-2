//Sample tlsserver using sslsockets
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;
public class tlsserver {
    // likely this port number is ok to use
    private static final int PORT = 8043;
    public static void main(String[] args) throws Exception {
// TrustStore
/* char[] passphrase_ts = "654321".toCharArray();
KeyStore ts = KeyStore.getInstance("JKS");
ts.load(new FileInputStream("truststore.jks"), passphrase_ts);
TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
tmf.init(ts); */
        TrustManager[] trustManagers = null; //tmf.getTrustManagers();
// set up key manager to do server authentication
        KeyManagerFactory kmf;
        KeyStore ks;
// First we need to load a keystore
        char[] passphrase = "123456".toCharArray();
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("srv-keystore.jks"), passphrase);
// Initialize a KeyManagerFactory with the KeyStore
        kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
        KeyManager[] keyManagers = kmf.getKeyManagers();
// Create an SSLContext to run TLS and initialize it with
        SSLContext context = SSLContext.getInstance("TLSv1.3");
        context.init(keyManagers, trustManagers, null);
// Create a SocketFactory that will create SSL server sockets.
        SSLServerSocketFactory ssf = context.getServerSocketFactory();
// Create socket and Wait for a connection
        ServerSocket ss = ssf.createServerSocket(PORT);
        System.out.println("TLS server running");
        SSLSocket s = (SSLSocket)ss.accept();
//s.setNeedClientAuth(true);
// below works only when client is authenticated
//SSLSession session = ((SSLSocket) s).getSession();
//Certificate[] cchain = session.getPeerCertificates();
//System.out.println("The Certificates used by the client");
//for (int i = 0; i < cchain.length; i++) {
// System.out.println(((X509Certificate) cchain[i]).getSubjectDN());
//};
// Get the input stream. En/Decryption happens transparently.
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
// Read through the input from the client and display it to the screen.
        System.out.println("TLS server waiting for input");
        String line = null;
        while (((line = in.readLine()) != null)) {
            System.out.println(line);
        }
        in.close();
        s.close();
    }
}
