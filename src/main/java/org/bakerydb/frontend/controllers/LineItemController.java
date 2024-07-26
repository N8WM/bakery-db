package org.bakerydb.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.bakerydb.backend.models.LineItem;
import org.bakerydb.backend.models.Order;

public class LineItemController extends  BaseTabController<LineItem> {

    @FXML
    private TableColumn<LineItem, String> dishNameTableColumn;
    @FXML
    private TableColumn<LineItem, Integer> quantityTableColumn;
    @FXML
    private TableColumn<LineItem, String> dishPriceTableColumn;
    @FXML
    private TableColumn<LineItem, String> totalPriceTableColumn;
    @FXML
    private TableColumn<LineItem, String> specialInstructionsTableColumn;

    Order order;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onInitialize(
            List.of(
                dishNameTableColumn,
                quantityTableColumn,
                dishPriceTableColumn,
                totalPriceTableColumn,
                specialInstructionsTableColumn
            ),
            List.of("dishName", "specialInstructions"),
            LineItem.class,
            "Inventory Item"
        );
    }

    public void setOrder(Order order) {
        LineItem.setOrderViewing(order);
    }
}
