package virtualprivatenetwork;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.Key;

public class VPNClient {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 8888);
            System.out.println("Connected to VPN Server");

            Key secretKey = new SecretKeySpec("YourSecretKey".getBytes(), "AES");

            InputStream serverIn = clientSocket.getInputStream();
            OutputStream serverOut = clientSocket.getOutputStream();

            Cipher encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);

            Cipher decryptCipher = Cipher.getInstance("AES");
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] sendData = "Hello, VPN Server!".getBytes();
            byte[] encryptedData = encryptCipher.doFinal(sendData);
            serverOut.write(encryptedData);

            byte[] buffer = new byte[1024];
            int bytesRead = serverIn.read(buffer);
            byte[] decryptedData = decryptCipher.doFinal(buffer, 0, bytesRead);
            System.out.println("Received from server: " + new String(decryptedData));

            clientSocket.close();
            System.out.println("Connection closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
