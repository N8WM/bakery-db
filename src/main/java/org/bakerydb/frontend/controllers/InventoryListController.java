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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import org.bakerydb.backend.models.InventoryItem;
import org.bakerydb.frontend.Main;
import org.bakerydb.util.Result;

import org.bakerydb.backend.DBUtil;

public class InventoryListController implements Initializable {

    @FXML
    private Label statusLabel;
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

        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("invId"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityTableColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitTableColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        reorderLevelTableColumn.setCellValueFactory(new PropertyValueFactory<>("reorderLevel"));

        FilteredList<InventoryItem> filteredData = new FilteredList<>(inventoryItemObservableList, b -> true);

        inventorySearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String q = newValue == null ? "" : newValue;
            filteredData.setPredicate(item -> {
                boolean empty = (q.isEmpty() || q.isBlank());
                boolean matched = item.getName().toLowerCase().indexOf(q.toLowerCase()) > -1;
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
        Result<ArrayList<InventoryItem>> wrapped = DBUtil.getInventoryUtil().fetchAll();

        if (wrapped.isErr()) {
            statusLabel.setText(wrapped.getError());
            this.inventoryItemObservableList.clear();
        } else {
            this.inventoryItemObservableList.setAll(wrapped.getValue());
        }
    }

    @FXML
    private void onRemoveAction() {
        InventoryItem selectedItem = inventoryTableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            inventoryItemObservableList.remove(selectedItem);
            DBUtil.getInventoryUtil().deleteItem(selectedItem.getInvId())
                .onError(m -> statusLabel.setText(m));
        }
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
            clone = item.clone();
        } else if (event.getSource().equals(addInventoryButton)) {
            isAdd = true;
            dialogTitle = "Add Inventory Item";
            item = new InventoryItem();
            clone = item.clone();
        } else {
            return;
        }

        FXMLLoader fxmlLoader = Main.loader("views/InventoryEditor.fxml");
        DialogPane dialoguePane;

        try {
            dialoguePane = fxmlLoader.load();
        } catch (IOException e) {
            statusLabel.setText(e.toString());
            return;
        }

        InventoryEditorController inventoryEditorController = fxmlLoader.getController();
        inventoryEditorController.setItem(clone);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialoguePane);
        dialog.setTitle(dialogTitle);

        Optional<ButtonType> clickedButton = dialog.showAndWait();
        if (!clickedButton.isPresent()) {
            statusLabel.setText("Dialog failed to open");
        } else if (clickedButton.get() == ButtonType.OK) {
            if (isAdd) {
                DBUtil.getInventoryUtil().newItem(clone)
                    .onSuccess(k -> clone.setInvId(k))
                    .onError(m -> statusLabel.setText(m));
                inventoryItemObservableList.add(clone);
            } else {
                item.update(clone);
                DBUtil.getInventoryUtil().update(item)
                    .onError(m -> statusLabel.setText(m));
            }
        }
    }

    @FXML
    private void onAddAction(ActionEvent event) {
        this.onUpdateAction(event);
    }
}
