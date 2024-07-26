package org.bakerydb.frontend.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.bakerydb.backend.models.Hours;

public class ClockInEditorController implements Initializable {

    @FXML
    private DialogPane dialogPane;
    @FXML
    private GridPane gridPane;
    @FXML
    private TextField emplIdField;

    private Hours item;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateFormData()) {
                event.consume();
            }
        });
    }

    public void setItem(Hours item) {
        this.item = item;
        if (item.getEmplId() != null) {
            emplIdField.setText(String.valueOf(item.getEmplId()));
        }
    }

    public boolean validateFormData() {
        try {
            int emplId = Integer.parseInt(emplIdField.getText());
            item.setEmplId(emplId);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid Employee ID");
            return false;
        }
    }

    @FXML
    private void onOkAction() {
        item.setEmplId(Integer.parseInt(emplIdField.getText()));
    }
}
