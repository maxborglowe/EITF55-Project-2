//Sample tlsclient using sslsockets
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;
public class tlsclient {
    private static final String HOST = "localhost";
    private static final int PORT = 8043;
    public static void main(String[] args) throws Exception {
// TrustStore
        char[] passphrase_ts = "654321".toCharArray();
        KeyStore ts = KeyStore.getInstance("JKS");
        ts.load(new FileInputStream("cl-truststore.jks"), passphrase_ts);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);
        TrustManager[] trustManagers = tmf.getTrustManagers();
        SSLContext context = SSLContext.getInstance("TLSv1.3");
        KeyManager[] keyManagers = null;
        context.init(keyManagers, trustManagers, new SecureRandom());
        SSLSocketFactory ssf = context.getSocketFactory();
        System.out.println("TLS client running");
        SSLSocket s = (SSLSocket) ssf.createSocket(HOST, PORT);
//s.socket.setEnabledCipherSuites(cipher_suites);
        OutputStream toserver = s.getOutputStream();
        SSLSession session = s.getSession();
        Certificate[] cchain = session.getPeerCertificates();
        System.out.println("The Certificates used by the server");
        for (int i = 0; i < cchain.length; i++) {
            System.out.println(((X509Certificate) cchain[i]).getSubjectDN());
        };
        toserver.write("\nConnection established.\n\n".getBytes());
        System.out.print("\nConnection established.\n\n");
        int inCharacter = 0;
        inCharacter = System.in.read();
        while (inCharacter != '~') {
            toserver.write(inCharacter);
            toserver.flush();
            inCharacter = System.in.read();
        }
        toserver.close();
        s.close();
    }
}