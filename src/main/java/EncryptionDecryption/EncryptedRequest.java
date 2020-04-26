package EncryptionDecryption;

/**
 * @author Thomas.Okonkwo on 11/03/2020 8:24 AM
 * @project Encryption and decryption project
 */
public class EncryptedRequest {
    private String IV;
    private String EncryptedData;
    private String HMAC;

    public String getIV() {
        return IV;
    }

    public void setIV(String IV) {
        this.IV = IV;
    }

    public String getEncryptedData() {
        return EncryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        EncryptedData = encryptedData;
    }

    public String getHMAC() {
        return HMAC;
    }

    public void setHMAC(String HMAC) {
        this.HMAC = HMAC;
    }
}
