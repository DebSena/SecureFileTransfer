import java.math.BigInteger;

public class RSA_encrypt {
    public static byte[] encrypt(byte[] message, BigInteger publicKey, BigInteger modulus) {
        return (new BigInteger(message)).modPow(publicKey, modulus).toByteArray();
    }
}
