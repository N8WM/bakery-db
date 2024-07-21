package org.bakerydb.frontend.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.bakerydb.backend.models.InventoryItem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;

public class InventoryEditorController implements Initializable {
    @FXML
    TextField nameTextField;
    @FXML
    TextField quantityTextField;
    @FXML
    TextField unitTextField;
    @FXML
    TextField reorderLevelTextField;
    @FXML
    DialogPane dialogPane;

    InventoryItem item;

    @Override
    public void initialize(URL url, ResourceBundle resource) {
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateFormData()) {
                event.consume();
            }
        });
    }

    public void setItem(InventoryItem item) {
        this.item = item;
        StringConverter<Number> converter = new NumberStringConverter();

        nameTextField.textProperty().bindBidirectional(item.nameProperty());
        quantityTextField.textProperty().bindBidirectional(item.quantityProperty(), converter);
        unitTextField.textProperty().bindBidirectional(item.unitProperty());
        reorderLevelTextField.textProperty().bindBidirectional(item.reorderLevelProperty(), converter);
    }

    public boolean validateFormData() {
        boolean isValid = true;
        if (nameTextField.getText().isEmpty()) {
            nameTextField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            nameTextField.setStyle(null);
        }
        if (quantityTextField.getText().isEmpty()) {
            quantityTextField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            quantityTextField.setStyle(null);
        }
        if (unitTextField.getText().isEmpty()) {
            unitTextField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            unitTextField.setStyle(null);
        }
        if (reorderLevelTextField.getText().isEmpty()) {
            reorderLevelTextField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            reorderLevelTextField.setStyle(null);
        }
        return isValid;
    }
}
