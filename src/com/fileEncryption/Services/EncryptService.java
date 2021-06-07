package com.fileEncryption.Services;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.*;
import javax.crypto.spec.*;
public class EncryptService {

    private String decyrptAlgorihtm = "PBKDF2WithHmacSHA512";
    private int iterations = 65536;
    private int keyLength = 128;
    private int saltBytesSize = 16;
    private String secureRandomSalt = "SHA1PRNG";
    public EncryptService() {

    }

    public String getSalt() throws NoSuchAlgorithmException {
        byte[] salt = new byte[saltBytesSize];
        SecureRandom.getInstance(secureRandomSalt).nextBytes(salt);
        return new String(salt);
    }

    public String encryptFile(char[] password, File file) throws Exception {
        byte[] encryptedKey = encryptPasswordWithPBKDF2(password);

        File inFile = new File(file.getPath()+".cif");
        inFile.createNewFile();
        File outFile = new File(file.getPath()+".hash");
        outFile.createNewFile();

        encryptFile(encryptedKey, inFile, outFile);
        return "OK";
    }

    public void generateShaKey(File inFile, File outFile) throws NoSuchAlgorithmException, IOException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        FileInputStream fileInStream = new FileInputStream(inFile);
        FileOutputStream fileOutStream = new FileOutputStream(FileDescriptor.out);

        byte[] data = new byte[1024];
        int read = 0;
        while ((read = fileInStream.read(data)) != -1) {
            sha1.update(data, 0, read);
        }
        ;
        byte[] hashBytes = sha1.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
            sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        String fileHash = sb.toString();

        fileOutStream.write(fileHash.getBytes(Charset.forName("UTF-8")));

        fileInStream.close();
        fileOutStream.close();
    }


    private byte[] encryptPasswordWithPBKDF2(char[] password) {
        try {
            String salt = getSalt();
            byte[] saltBytes = salt.getBytes();
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(decyrptAlgorihtm);
            PBEKeySpec keySpec = new PBEKeySpec(password, saltBytes, iterations, keyLength);
            SecretKey key = secretKeyFactory.generateSecret(keySpec);
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

        int bufferBytes = Math.min(fileInputStream.available(), 64);
        byte[] buffer = new byte[bufferBytes];

        while (buffer.length == 64) {
            fileInputStream.read(buffer);
            byte[] encryptedBuffer = cf.update(buffer);
            fileOutputStream.write(encryptedBuffer);
            bufferBytes = Math.min(fileInputStream.available(), 64);
            buffer = new byte[bufferBytes];
        }

        fileInputStream.read(buffer);
        byte[] encryptedBuffer = cf.doFinal(buffer);
        fileOutputStream.write(encryptedBuffer);

        fileInputStream.close();
        fileOutputStream.close();
    }

}
