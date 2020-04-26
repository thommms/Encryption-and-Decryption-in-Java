package EncryptionDecryption;


import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by Thomas Okonkwo on 26th April 2020
 */

public class SecurityUtil {

    private static String encryptionKey;
    private static String ivSpec;

    public SecurityUtil(String encryptionKey, String ivSpec) {
        this.encryptionKey = encryptionKey;
        this.ivSpec = ivSpec;
    }

    private static int iterations = 65536;
    private static int keySize = 256;
    private static byte[] ivBytes = null;
    private static Cipher cipherEncrypt, cipherDecrypt;
    public static Logger LOGGER = LoggerFactory.getLogger(SecurityUtil.class.getName());

    public static String encryptMessage(String value, String ivSpec, String encryptionKey){
            try {
                IvParameterSpec iv = new IvParameterSpec(Base64.decodeBase64(ivSpec));
                SecretKeySpec skeySpec = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

                byte[] encrypted = cipher.doFinal(value.getBytes());
                return Base64.encodeBase64String(encrypted);
            } catch (NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | UnsupportedEncodingException e) {
                throw new IllegalArgumentException("Failed to encrypt");
            }
    }

    public static StringBuilder randomLetters(int n) {
        String AlphabetString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (AlphabetString.length() * Math.random());
            sb.append(AlphabetString.charAt(index));
        }
        return  sb;
    }

    public static String randomStringsForIv(int n) {
        String AlphabetString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (AlphabetString.length() * Math.random());
            sb.append(AlphabetString.charAt(index));
        }
        return  sb.toString();
    }

    public static String decryptMessage(String encrypted, String secretKey, String ivSpec) {
            try {
                IvParameterSpec iv = new IvParameterSpec(Base64.decodeBase64(ivSpec));
                SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

                byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

                return new String(original);
            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | UnsupportedEncodingException e) {
                throw new IllegalArgumentException(" failed to decrypt");
            }
    }

    public static void main(String[] args) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
       //Decryption
        /**
         * For the decryption, pass in
         * @EncryptedData : the encrypted data you want to decrypted
         * @SecretKey : the client secret key
         * @IVSpec : randomly generated iv (24 bit characters)
         */
        String encrypted = "xxxxxxxxxxxxxxxencryptedDataxxxxxxxxxxxxx==";
        String secretKey = "xxxxxsecretKeyxxxxxxxx";
        String ivSpec = "xxxxxivdata+spec==";

//        System.out.println("Decrypted value:\n"+ decryptMessage(encrypted, secretKey, ivSpec)+"\n");

        //Encryption
        /**
         * for encryption, pass in
         * @Body that contains the request to be encrypted
         * @IV : a randomly generated 24 bit characters
         */

        String currentDate = DateUtil.formatDate(new Date());

        String plainBody ="{ \n\"RequestReference\":\""+randomLetters(32)+"\""+", \n\"AccountNumber\": \"12345678\", \n\"BankCode\":\"076\" \n}";



        String iv = java.util.Base64.getEncoder().encodeToString(randomStringsForIv(16).getBytes());

        String encryptedData = encryptMessage(plainBody,iv, secretKey );
        System.out.println(plainBody+"\n");

        String encryptedDataJoin =  String.format("%s%s", iv, encryptedData);
//        generating HMAC with encrypted data
        String hmac = HashUtil.hash(encryptedDataJoin, secretKey, HashUtil.HashAlgorithm.HmacSHA512);

        EncryptedRequest encryptedRequest = new EncryptedRequest();
        encryptedRequest.setIV(iv);
        encryptedRequest.setHMAC(hmac);
        encryptedRequest.setEncryptedData(encryptedData);

        Gson gson = new Gson();
        String jsonEncryptedRequest = gson.toJson(encryptedRequest);

//        System.out.println(jsonEncryptedRequest);

        System.out.println("{\n"+"\"IV\":"+"\""+iv+"\""+"," +
                "\n\"HMAC\":"+"\""+hmac+"\""+"," +
                "\n\"EncryptedData\":"+"\""+encryptedData+"\"" +
                "\n}");
    }

}