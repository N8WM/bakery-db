package org.bakerydb.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.bakerydb.backend.models.InventoryItem;

public class InventoryListController extends  BaseTabController<InventoryItem> {

    @FXML
    private TableColumn<InventoryItem, Integer> idTableColumn;
    @FXML
    private TableColumn<InventoryItem, String> nameTableColumn;
    @FXML
    private TableColumn<InventoryItem, Float> quantityTableColumn;
    @FXML
    private TableColumn<InventoryItem, String> unitTableColumn;
    @FXML
    private TableColumn<InventoryItem, Float> reorderLevelTableColumn;
    @FXML
    private TableColumn<InventoryItem, Boolean> restockTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onInitialize(
            List.of(
                idTableColumn,
                nameTableColumn,
                quantityTableColumn,
                unitTableColumn,
                reorderLevelTableColumn,
                restockTableColumn
            ),
            List.of("name"),
            InventoryItem.class,
            "Inventory Item"
        );
    }
}
