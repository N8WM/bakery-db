package org.bakerydb.frontend.controllers;

import java.net.URL;
import java.util.ArrayList;
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
    public void initialize(URL location, ResourceBundle resources) {
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
        ArrayList<TextField> fields = new ArrayList<>() {{
            add(nameTextField);
            add(quantityTextField);
            add(unitTextField);
            add(reorderLevelTextField);
        }};

        boolean isValid = true;

        for (TextField field : fields) {
            if (
                field.getText() == null ||
                field.getText().isEmpty() ||
                field.getText().isBlank()
            ){
                field.setStyle("-fx-border-color: red;");
                isValid = false;
            } else {
                field.setStyle(null);
            }
        }

        return isValid;
    }
}
