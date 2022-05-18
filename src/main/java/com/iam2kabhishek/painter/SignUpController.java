package com.iam2kabhishek.painter;

import java.io.IOException;
import javafx.fxml.FXML;

public class SignUpController {

    @FXML
    private javafx.scene.control.TextField userSignup;

    @FXML
    private javafx.scene.control.PasswordField passSignup;

    @FXML
    private void registerUser() throws IOException {
        UserModel userModel = new UserModel();
        if (!(userSignup.getText().isEmpty() || passSignup.getText().isEmpty())) {
            userModel.addUser(userSignup.getText(), passSignup.getText());
            App.setRoot("login");
        } else {
            System.out.println("Empty data fields");
        }
    }
}
