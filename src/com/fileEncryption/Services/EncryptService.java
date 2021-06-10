package com.fileEncryption.Services;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.*;
import javax.crypto.spec.*;
public class EncryptService {

    public final static String SHA_1 = "SHA1";
    public final static int SHA_KEY_LENGTH = 1024;
    public final static String SALT = "4321";
    public final static int ITERATIONS = 10000;
    public final static int KEY_LENGTH = 128;
    public final static String  ENCRYPT_KEY_ALGORITHM = "PBKDF2WithHmacSHA512";

    public EncryptService() {

    }


    public String encryptFile(char[] password, File originalFile) throws Exception {
        byte[] key = encryptPasswordWithPBKDF2(password);
        File inFile = new File(originalFile.getPath()+".cif");
        inFile.createNewFile();
        File outFile = new File(originalFile.getPath()+".hash");
        outFile.createNewFile();

        encryptFile(key, originalFile, inFile);
        writeSha1Key(originalFile, outFile);
        return "OK";
    }

    private void writeSha1Key(File originalFile, File fileOut) throws NoSuchAlgorithmException, IOException {
        MessageDigest sha1 = MessageDigest.getInstance(SHA_1);
        FileInputStream fis = new FileInputStream(originalFile);
        FileOutputStream fos = new FileOutputStream(fileOut);

        byte[] data = new byte[SHA_KEY_LENGTH];
        int read = 0;
        while ((read = fis.read(data)) != -1) {
            sha1.update(data, 0, read);
        }

        byte[] hashBytes = sha1.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        String fileHash = sb.toString();

        fos.write(fileHash.getBytes(Charset.forName("UTF-8")));

        fis.close();
        fos.close();
    }


    private byte[] encryptPasswordWithPBKDF2(char[] password) {
        try {
            byte[] salt = SALT.getBytes();
            SecretKeyFactory kf = SecretKeyFactory.getInstance(ENCRYPT_KEY_ALGORITHM);
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKey key = kf.generateSecret(spec);
            return key.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static void encryptFile(byte[] key, File in, File out) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        KeySpec keySpec = new SecretKeySpec(key, "AES");
        Cipher cf = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cf.init(Cipher.ENCRYPT_MODE, (SecretKeySpec) keySpec);

        FileInputStream fileInputStream = new FileInputStream(in);
        FileOutputStream fileOutputStream = new FileOutputStream(out);


        // Determine the size of the buffer
        int bufferBytes = Math.min(fileInputStream.available(), 64);
        byte[] buffer = new byte[bufferBytes];

        // While remaining bytes still fit in a 64byte buffer.
        while (buffer.length == 64) {
            fileInputStream.read(buffer);
            byte[] encryptedBuffer = cf.update(buffer);
            fileOutputStream.write(encryptedBuffer);
            bufferBytes = Math.min(fileInputStream.available(), 64);
            buffer = new byte[bufferBytes];
        }

        // Last portion of data
        fileInputStream.read(buffer);
        byte[] encryptedBuffer = cf.doFinal(buffer);
        fileOutputStream.write(encryptedBuffer);

        fileInputStream.close();
        fileOutputStream.close();
    }

}
