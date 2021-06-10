package com.fileEncryption.Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class DecrypterService {
	
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES";
	private static final String ALGORITHMD = "PBKDF2WithHmacSHA512";
	public final static int ITERATIONS = 10000;
	public final static int KEY_LENGTH = 128;
	public final static String SALT = "1234";
	private String secureRandomSalt = "SHA1PRNG";
	private int saltBytess = 16;

	public DecrypterService() {
		
	}
	
	public byte[] PBKDF2(char[] password) {
		try {
			
            byte[] saltBytes = SALT.getBytes();
			SecretKeyFactory keyfactor = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(password, saltBytes, ITERATIONS, KEY_LENGTH);
			SecretKey key = keyfactor.generateSecret(spec);
			return key.getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	
	public String decrypt(byte[] key, File inputFile, File outputFile) throws Exception {
		
		//byte[] key = PBKDF2(password);
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[16]));
		
		FileInputStream inputStream = new FileInputStream(inputFile);
		FileOutputStream outputStream = new FileOutputStream(outputFile);
		
		int bufferBytes = Math.min(inputStream.available(), 64);
		byte[] buffer = new byte[bufferBytes];
		
		while (buffer.length == 64) {
			inputStream.read(buffer);
			byte[] encryptedBuffer = cipher.update(buffer);
			outputStream.write(encryptedBuffer);
			bufferBytes = Math.min(inputStream.available(), 64);
			buffer = new byte[bufferBytes];
		}
		
		inputStream.read(buffer);
		byte[] encryptedBuffer = cipher.doFinal(buffer);
		outputStream.write(encryptedBuffer);
		
		inputStream.close();
	    outputStream.close();
		
		return "OK";
	}
	
	
	
	
	public boolean verifySHA1(File file, File hash) throws Exception {
		String inHash = null;
		BufferedReader buffer = new BufferedReader(new FileReader(hash));
		while ((inHash = buffer.readLine()) != null) {
			inHash = inHash.trim();
			break;
		}
		buffer.close();
		
		String fileHash = SHA1(file);
		
		return fileHash.equals(inHash);
		
	}
	
	
	public String SHA1(File file) throws Exception {
		MessageDigest sha1 = MessageDigest.getInstance("SHA1");
		FileInputStream inputStream = new FileInputStream(file);

		byte[] data = new byte[1024];
		int read = 0;
		while ((read = inputStream.read(data)) != -1) {
			sha1.update(data, 0, read);
		}
		;
		byte[] hashBytes = sha1.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < hashBytes.length; i++) {
			sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		String fileHash = sb.toString();
		inputStream.close();
		return fileHash;
	}
	
	
}
