package com.iam2kabhishek.painter;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private javafx.scene.control.Button loginButton;
    @FXML
    private javafx.scene.control.TextField userLogin;
    @FXML
    private javafx.scene.control.PasswordField passLogin;

    @FXML
    private void loginUser() throws IOException {
        UserModel userModel = new UserModel();

        if (userModel.canLogin(userLogin.getText(), passLogin.getText())) {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

            App.multiLaunch(Painter.class);
        } else {
            switchToSignup();
        }
    }

    @FXML
    private void switchToSignup() throws IOException {
        App.setRoot("signup");
    }

    @FXML
    private void switchToAbout() throws IOException {
        App.setRoot("about");
    }
}
