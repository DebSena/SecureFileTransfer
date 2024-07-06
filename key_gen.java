import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

public class key_gen {
    private BigInteger privateKey;
    private BigInteger publicKey;
    private BigInteger modulus;

    public key_gen(int bit_length){
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(bit_length / 2, random);
        BigInteger q = BigInteger.probablePrime(bit_length / 2, random);
        modulus = p.multiply(q);

        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        publicKey = BigInteger.probablePrime(bit_length / 2, random);
        while (phi.gcd(publicKey).intValue() > 1) {
            publicKey = BigInteger.probablePrime(bit_length / 2, random);
        }
        privateKey = publicKey.modInverse(phi);
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public BigInteger getModulus() {
        return modulus;
    }
    public void saveKeys() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("public.key"))) {
            oos.writeObject(publicKey);
            oos.writeObject(modulus);
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("private.key"))) {
            oos.writeObject(privateKey);
            oos.writeObject(modulus);
        }
    }

    public static void main(String[] args) throws IOException {
        key_gen keyPair = new key_gen(2048);
        keyPair.saveKeys();
        System.out.println("Keys generated and saved.");
    }
}
