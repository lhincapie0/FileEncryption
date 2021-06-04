package com.fileEncryption.Services;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
public class EncryptService {

    public EncryptService() {

    }

    public String getSalt() throws NoSuchAlgorithmException {
        byte[] salt = new byte[20];
        SecureRandom.getInstance("SHA1PRNG").nextBytes(salt);
        return new String(salt);
    }

//    public byte[] encryptPasswordWithPBKDF2(char[] password) {
//        try {
//            String salt = getSalt();
//            byte[] saltBytes = salt.getBytes();
//            def skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512"); // only in java 8, uncomment above for java 7
//
//        } catch (NoSuchAlgorithmException e) {
//            System.out.println("Error: Unable to encrypt password");
//            e.printStackTrace();
//        }
//        return new byte[2];
//    }
}
