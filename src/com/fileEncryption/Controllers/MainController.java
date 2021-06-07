package com.fileEncryption.Controllers;

import com.fileEncryption.Services.EncryptService;
import com.fileEncryption.view.View;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {

    private View view;

    private EncryptService encryptService;
    public MainController(View v){
        view = v;
        encryptService = new EncryptService();
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
}
