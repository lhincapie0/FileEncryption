package com.fileEncryption.Controllers;

import com.fileEncryption.Services.DecrypterService;
import com.fileEncryption.Services.EncryptService;
import com.fileEncryption.view.View;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {

    private View view;

    private EncryptService encryptService;
    
    private DecrypterService decrypterService;
    public MainController(View v){
        view = v;
        encryptService = new EncryptService();
        decrypterService = new DecrypterService();
    }

    public void encryptFile(File file, char[] password) throws IOException {
        String message = null;
        try {
            message = encryptService.encryptFile(password, file);
        } catch (Exception e) {
            e.printStackTrace();
            message = "ERROR";
        }

        view.notifyEncryptFinished(message, file);
    }
    
    
    public void decryptFile(char[] password, File fileToDecrypt, File hashFile) throws IOException {
    	String path = fileToDecrypt.getAbsolutePath();
		path = path.substring(0, path.length() - 4);
		File outDecrypt = new File(path);
		
		byte[] key = decrypterService.PBKDF2(password);
		
		String message = null;
        try {	
        	//System.out.println("verifySHA1: "+ decrypterService.verifySHA1(outDecrypt, hashFile));
        	
        	if(decrypterService.verifySHA1(outDecrypt, hashFile)) {
        		 message = decrypterService.decrypt(key, fileToDecrypt, outDecrypt);
        	}
           
        } catch (Exception e) {
            e.printStackTrace();
            message = "ERROR";
        }
        
        view.notifyDencryptFinished(message, fileToDecrypt);
		
    	
    }
}
