import java.io.*;
import java.net.Socket;
import java.math.BigInteger;

public class SecureFileClient {
    public static void main(String[] args) {
        // Load RSA private key and modulus
        BigInteger privateKey;
        BigInteger modulus;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("private.key"))) {
            privateKey = (BigInteger) ois.readObject();
            modulus = (BigInteger) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try (Socket socket = new Socket("localhost", 5000);
             InputStream inputStream = socket.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream("received_file.txt");
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {

            System.out.println("Connecting to server...");

            // Receive the encrypted file
            byte[] byteArray = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int bytesRead;
            while ((bytesRead = inputStream.read(byteArray)) != -1) {
                byteArrayOutputStream.write(byteArray, 0, bytesRead);
            }

            byte[] encryptedData = byteArrayOutputStream.toByteArray();

            //Decrypt the file
            byte[] decryptedData = RSA_dcrypt.decrypt(encryptedData, privateKey, modulus);
            bufferedOutputStream.write(decryptedData);

            System.out.println("File received and decrypted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
