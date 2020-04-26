package EncryptionDecryption;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Thomas Okonkwo on 26th April 2020
 */

public class HashUtil {

    public enum HashAlgorithm {
        SHA_256, SHA_512, HmacSHA512;

        @Override
        public String toString(){
            return super.toString().replace('_', '-');
        }
    }

    public static String hash(String input, HashAlgorithm hashAlgorithm) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance(hashAlgorithm.toString());

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return bytesToHex(md.digest(input.getBytes(StandardCharsets.UTF_8)));
    }

    public static String hash(String input, String secretKey, HashAlgorithm hashAlgorithm) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        byte [] byteKey = secretKey.getBytes("UTF-8");
        Mac sha512_HMAC = Mac.getInstance(hashAlgorithm.toString());

        SecretKeySpec keySpec = new SecretKeySpec(byteKey, hashAlgorithm.toString());
        sha512_HMAC.init(keySpec);
        byte [] macData = sha512_HMAC.doFinal(input.getBytes("UTF-8"));
        return bytesToHex(macData);
    }


    public static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
