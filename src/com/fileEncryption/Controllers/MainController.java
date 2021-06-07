package com.fileEncryption.Controllers;

import com.fileEncryption.Services.EncryptService;
import com.fileEncryption.view.View;

import java.io.File;

public class MainController {

    private View view;

    private EncryptService encryptService;
    public MainController(View v){
        view = v;
        encryptService = new EncryptService();
    }

    public void encryptFile(File file, char[] password) {
        String result = null;
        try {
            result = encryptService.encryptFile(password, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(result);

    }
}
