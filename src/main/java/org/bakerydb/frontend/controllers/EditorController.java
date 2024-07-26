package org.bakerydb.frontend.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.bakerydb.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;

public class EditorController implements Initializable {

    @FXML
    DialogPane dialogPane;
    @FXML
    GridPane gridPane;

    private Model<?> model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!validateFormData()) {
                event.consume();
            }
        });
    }

    public void setItem(Model<?> item) {
        model = item;
        ArrayList<ModelAttribute<?>> attrs = model.getVisibleAttributes();
        for (int i = 0; i < attrs.size(); i++) {
            ModelAttribute<?> p = attrs.get(i);
            if (p.isUserEditable())
                p.addToGrid(gridPane, i);
        }
    }

    public boolean validateFormData() {
        return model.validate();
    }
}
