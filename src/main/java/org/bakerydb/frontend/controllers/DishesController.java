package org.bakerydb.frontend.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import org.bakerydb.backend.models.Dishes;
import org.bakerydb.frontend.FEUtil;

public class DishesController extends BaseTabController<Dishes> {

    @FXML
    private TableColumn<Dishes, Integer> idTableColumn;
    @FXML
    private TableColumn<Dishes, String> nameTableColumn;
    @FXML
    private TableColumn<Dishes, Float> priceTableColumn;
    @FXML
    private TableColumn<Dishes, String> categoryTableColumn;
    @FXML
    private TableColumn<Dishes, String> descriptionTableColumn;
    @FXML
    private TableColumn<Dishes, String> ingredientsTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onInitialize(
            List.of(idTableColumn, nameTableColumn, priceTableColumn, categoryTableColumn, descriptionTableColumn, ingredientsTableColumn),
            List.of("name"),
            Dishes.class,
            "Dishes"
        );

        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("dishId"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryTableColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionTableColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        ingredientsTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.join(", ", cellData.getValue().getIngredients())));
    }
}
