package org.bakerydb.frontend.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.bakerydb.backend.models.LineItem;
import org.bakerydb.backend.models.Order;
import org.bakerydb.frontend.FEUtil;

public class OrderController extends  BaseTabController<Order> {

    @FXML
    private TableColumn<Order, Integer> idTableColumn;
    @FXML
    private TableColumn<Order, String> ccnTableColumn;
    @FXML
    private TableColumn<Order, Date> dateTableColumn;
    @FXML
    private TableColumn<Order, Integer> emplIdTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onInitialize(
            List.of(
                idTableColumn,
                ccnTableColumn,
                dateTableColumn,
                emplIdTableColumn
            ),
            List.of("name"),
            Order.class,
            "Inventory Item",
            (order) -> {
                LineItem.setOrderViewing(order);

                FXMLLoader fxmlLoader = FEUtil.loader("views/LineItems.fxml");
                DialogPane dialogPane;

                try {
                    dialogPane = fxmlLoader.load();
                } catch (IOException e) {
                    FEUtil.showStatusMessage("Error Opening LineItems View", e.toString(), true);
                    e.printStackTrace();
                    return;
                }

                FEUtil.showCustomDialog(dialogPane, "Order for " + (String) order.getAttribute("ccn").getValue());
            }
        );
    }

    @Override
    @FXML
    public void onAddAction() {
        Order newOrder = new Order(null, null, Date.valueOf(LocalDate.now()), null);

        FEUtil.showAddEditor(
            newOrder,
            "Add Employee",
            this.getObservableList()
        );
    }
}
