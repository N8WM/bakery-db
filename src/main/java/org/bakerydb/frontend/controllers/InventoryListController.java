package org.bakerydb.frontend.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.bakerydb.backend.models.InventoryItem;
import org.bakerydb.frontend.FEUtil;
import org.bakerydb.util.ModelAttribute;
import org.bakerydb.util.Result;

public class InventoryListController implements Initializable {

    @FXML
    private TextField inventorySearchTextField;
    @FXML
    private TableView<InventoryItem> inventoryTableView;
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
    private Button refreshInventoryButton;
    @FXML
    private Button removeInventoryButton;
    @FXML
    private Button updateInventoryButton;
    @FXML
    private Button addInventoryButton;

    ObservableList<InventoryItem> inventoryItemObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.onRefreshAction();

        idTableColumn.setCellValueFactory(c -> c.getValue().getAttribute("invId").getUncheckedProperty());
        nameTableColumn.setCellValueFactory(c -> c.getValue().getAttribute("name").getUncheckedProperty());
        quantityTableColumn.setCellValueFactory(c -> c.getValue().getAttribute("quantity").getUncheckedProperty());
        unitTableColumn.setCellValueFactory(c -> c.getValue().getAttribute("unit").getUncheckedProperty());
        reorderLevelTableColumn.setCellValueFactory(c -> c.getValue().getAttribute("reorderLevel").getUncheckedProperty());

        FilteredList<InventoryItem> filteredData = new FilteredList<>(inventoryItemObservableList, b -> true);

        inventorySearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String q = newValue == null ? "" : newValue;
            filteredData.setPredicate(item -> {
                boolean empty = (q.isEmpty() || q.isBlank());
                ModelAttribute<String> attr = item.getAttribute("Name");
                boolean matched = attr.getValue().toLowerCase().indexOf(q.toLowerCase()) > -1;
                return empty || matched;
            });
        });

        SortedList<InventoryItem> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(inventoryTableView.comparatorProperty());

        inventoryTableView.setItems(sortedData);

        TableViewSelectionModel<InventoryItem> selectionModel = inventoryTableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(observable -> {
            removeInventoryButton.setDisable(selectionModel.getSelectedItem() == null);
            updateInventoryButton.setDisable(selectionModel.getSelectedItem() == null);
        });

        removeInventoryButton.setDisable(true);
    }

    @FXML
    private void onRefreshAction() {
        Result<ArrayList<InventoryItem>> wrapped = new InventoryItem().fetchAllDB();

        if (wrapped.isErr()) {
            FEUtil.showStatusMessage(
                "Error Refreshing Page",
                wrapped.getError(),
                true
            );
            this.inventoryItemObservableList.clear();
        } else {
            this.inventoryItemObservableList.setAll(wrapped.getValue());
        }
    }

    @FXML
    private void onRemoveAction() {
        InventoryItem selectedItem = inventoryTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            selectedItem.deleteFromDB()
                .onSuccess(() -> inventoryItemObservableList.remove(selectedItem))
                .onError(m -> FEUtil.showStatusMessage("Error Removing Item", m, true));
    }

    @FXML
    private void onUpdateAction(ActionEvent event) {
        final InventoryItem item, clone;
        String dialogTitle = "";
        boolean isAdd;

        if (event.getSource().equals(updateInventoryButton)) {
            isAdd = false;
            dialogTitle = "Update Inventory Item";
            item = inventoryTableView.getSelectionModel().getSelectedItem();
        } else if (event.getSource().equals(addInventoryButton)) {
            isAdd = true;
            dialogTitle = "Add Inventory Item";
            item = new InventoryItem();
        } else {
            return;
        }

        clone = item.clone();

        FXMLLoader fxmlLoader = FEUtil.loader("views/Editor.fxml");
        DialogPane dialogPane;

        try {
            dialogPane = fxmlLoader.load();
        } catch (IOException e) {
            FEUtil.showStatusMessage("Error Opening Editor", e.getMessage(), true);
            return;
        }

        EditorController inventoryEditorController = fxmlLoader.getController();
        inventoryEditorController.setItem(clone);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(dialogTitle);

        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (!clickedButton.isPresent()) {
            FEUtil.showStatusMessage(
                "JavaFX Error",
                "Node was not fully loaded",
                true
            );
        } else if (clickedButton.get() == ButtonType.OK) {
            if (isAdd) {
                clone.addToDB("invId")
                    .onSuccess(k -> {
                        item.update(clone);
                        inventoryItemObservableList.add(clone);
                    })
                    .onError(m -> FEUtil.showStatusMessage("Failed to Add Item", m, true));
            } else {
                clone.updateDB()
                    .onSuccess(() -> item.update(clone))
                    .onError(m -> FEUtil.showStatusMessage("Failed to Update Item", m, true));
            }
        }
    }

    @FXML
    private void onAddAction(ActionEvent event) {
        this.onUpdateAction(event);
    }
}
