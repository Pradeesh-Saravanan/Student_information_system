package com.example.master;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.*;
public class StudentController {

    @FXML
    private Button loginbtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    protected void onLoginButtonClick() {
        String username = usernameField.getText();
        String pass = passwordField.getText();
        if(username.equals("admin")&&pass.equals("password")){
            System.out.println("test successful");
        }
    }
}