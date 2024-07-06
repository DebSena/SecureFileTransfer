import java.math.BigInteger;

public class RSA_dcrypt {
    public static byte[] decrypt(byte[] encryptedMessage, BigInteger privateKey, BigInteger modulus) {
        return (new BigInteger(encryptedMessage)).modPow(privateKey, modulus).toByteArray();
    }
}
