package com.fileEncryption.Controllers;

import com.fileEncryption.view.View;

import java.io.File;

public class MainController {

    private View view;

    public MainController(View v){
        view = v;
    }

    public void encryptFile(File file, String password) {
        System.out.println("PASSWORD");
        System.out.println(password);
    }
}
