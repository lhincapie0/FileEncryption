package com.fileEncryption;

import com.fileEncryption.Controllers.MainController;
import com.fileEncryption.view.View;

import java.io.IOException;

import static com.fileEncryption.constants.Constants.MAIN_VIEW_TITLE;

public class Main {

    public static void main(String[] args) throws IOException {
        View mainView = new View(MAIN_VIEW_TITLE);
        MainController mainController = new MainController(mainView);

        mainView.setMainController(mainController);
    }
}