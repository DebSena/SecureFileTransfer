import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.math.BigInteger;

public class SecureFileServer {
    public static void main(String[] args) {
        // Load RSA public key and modulus
        BigInteger publicKey;
        BigInteger modulus;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("public.key"))) {
            publicKey = (BigInteger) ois.readObject();
            modulus = (BigInteger) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server waiting for client on port 5000");

            while (true) {
                try (Socket socket = serverSocket.accept();
                     InputStream inputStream = socket.getInputStream();
                     OutputStream outputStream = socket.getOutputStream()) {

                    System.out.println("Accepted connection: " + socket);

                    // Read the file
                    File file = new File("data.txt");
                    byte[] byteArray = new byte[(int) file.length()];
                    FileInputStream fileInputStream = new FileInputStream(file);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    bufferedInputStream.read(byteArray, 0, byteArray.length);

                    // Encrypt the file
                    byte[] encryptedData = RSA_encrypt.encrypt(byteArray, publicKey, modulus);
                    outputStream.write(encryptedData);

                    System.out.println("File transfer complete");
                    bufferedInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
