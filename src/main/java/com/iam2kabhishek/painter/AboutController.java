package com.iam2kabhishek.painter;

import java.io.IOException;

import javafx.fxml.FXML;

public class AboutController {
    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }
}
