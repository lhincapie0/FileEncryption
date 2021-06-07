package com.fileEncryption.view;
import com.fileEncryption.Controllers.MainController;
import com.fileEncryption.Services.EncryptService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class View extends JFrame implements ActionListener, DocumentListener {

    // Service definitions

    // Controller definition
    private MainController mainController;

    // Component definitions
    private JLabel passwordLabel;
    private JPasswordField passwordTextField;
    private JButton selectEncryptFileButton;

    private JFileChooser fileReaderEncryptFile;

    private JButton encryptButton;
    private File fileToEncrypt;

    private JLabel helperTextLabel;
    /**
     * String labels
     */
    private String encryptButtonLabel = "Encrypt file";
    private String passwordEncryptLabel = "Password to encrypt file";
    private String selectEncryptFile = "Upload file to encrypt";


    public View(String viewTitle) {
        renderMainFrame(viewTitle);
        renderEncryptComponents();
        refreshAfterRender();
    }

    public void setMainController(MainController controller) {
        mainController = controller;
    }

    public void renderMainFrame(String viewTitle) {
        setTitle(viewTitle);
        setSize(800,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon("src/com/fileEncryption/view/decryptEncrypt.png")));
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
    }

    private void renderEncryptComponents() {
        // Password label
        passwordLabel = new JLabel(passwordEncryptLabel);
        // Password text field
        passwordTextField = new JPasswordField();
        passwordTextField.setColumns(30);
        passwordTextField.addActionListener(this);
        passwordTextField.getDocument().addDocumentListener(this);

        // Button to select file
        selectEncryptFileButton = new JButton(selectEncryptFile);
        selectEncryptFileButton.setBounds(50,100,60,110);

        // Encrypt button
        encryptButton = new JButton(encryptButtonLabel);
        encryptButton.setEnabled(false);
        encryptButton.addActionListener(this);

        // Components added to layout
        add(passwordLabel);
        add(passwordTextField);
        add(selectEncryptFileButton);
        add(encryptButton);

        // Text helper
        helperTextLabel = new JLabel("");
        add(helperTextLabel);

        addActionToEncryptButton(selectEncryptFileButton);
    }

    private void addActionToEncryptButton(JButton button) {
        button.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int selection = fileChooser.showOpenDialog(this);

            if(selection == JFileChooser.APPROVE_OPTION) {
                fileToEncrypt = fileChooser.getSelectedFile();
                encryptButton.setEnabled(true);
            }else {
                JOptionPane.showMessageDialog(this, "Please select a file to encrypt", "Invalid Value",JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void refreshAfterRender() {
        setSize(799,799);
        setSize(800,800);
    }

    /**
     * Action Listeners
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == encryptButton) {
            try {
                mainController.encryptFile(fileToEncrypt, passwordTextField.getPassword());
            } catch (IOException ioException) {
                ioException.printStackTrace();
                helperTextLabel.setText("Please wait while file is encrypted... This won't take much");
                encryptButton.setEnabled(false);
            }
        }
    }

    public void notifyEncryptFinished(String result, File file) throws IOException {
        String message = "";
        if (result == "OK") {
            message = "Successfully encrypted file: " +file.getCanonicalPath();
        } else {
            message = "Failed to encrypt file: " +file.getCanonicalPath();
        }
        encryptButton.setEnabled(false);
        helperTextLabel.setText("");
        passwordTextField.setText("");
        JOptionPane.showMessageDialog(this, message, "Encrypt finished.", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (e.getDocument() == passwordTextField.getDocument() && fileToEncrypt != null) {
            encryptButton.setEnabled(true);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        if (e.getDocument() == passwordTextField.getDocument() && String.valueOf(passwordTextField.getPassword()).equals("")) {
            encryptButton.setEnabled(false);
        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}