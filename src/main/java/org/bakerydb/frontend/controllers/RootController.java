package org.bakerydb.frontend.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class RootController implements Initializable {

    @FXML TabPane tabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) { }

    public ObservableList<Tab> getTabs() {
        return tabPane.getTabs();
    }
}
