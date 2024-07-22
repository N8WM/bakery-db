package org.bakerydb.frontend.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class StatusMessageController implements Initializable {

    @FXML
    private Label messageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setError(boolean error) {
        if (error) {
            messageLabel.setStyle("-fx-text-fill: red;");
        } else {
            messageLabel.setStyle("-fx-text-fill: black;");
        }
    }
}
