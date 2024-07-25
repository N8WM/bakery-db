package org.bakerydb.frontend.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.bakerydb.backend.DBUtil;
import org.bakerydb.frontend.FEUtil;
import org.bakerydb.util.Model;
import org.bakerydb.util.ModelAttribute;
import org.bakerydb.util.Result;

public abstract class BaseTabController<T extends Model<T>> implements Initializable {

    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<T> tableView;
    @FXML
    private Button removeButton;
    @FXML
    private Button updateButton;

    private ArrayList<TableColumn<T, ?>> tableColumns;
    private ObservableList<T> observableList = FXCollections.observableArrayList();
    private ArrayList<String> searchableAttributes;
    private Class<T> modelClass;
    private String modelItemName;

    /**
     * This method must be called by the subclass to initialize the table view
     */
    public void onInitialize(
        Collection<TableColumn<T, ?>> tableColumns,
        Collection<String> searchableAttributes,
        Class<T> modelClass,
        String modelItemName
    ) {
        this.tableColumns = new ArrayList<>(tableColumns);
        this.searchableAttributes = new ArrayList<>(searchableAttributes);
        this.modelClass = modelClass;
        this.modelItemName = modelItemName;

        this.refresh(false);

        for (int i = 0; i < this.tableColumns.size(); i ++) {
            int _index = i;
            this.tableColumns.get(_index).setCellValueFactory(
                d -> d.getValue().getAttributes().get(_index).getUncheckedProperty()
            );
        }

        FilteredList<T> filteredData = new FilteredList<>(observableList, b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            String q = newValue == null ? "" : newValue;
            filteredData.setPredicate(item -> {
                // boolean empty = (q.isEmpty() || q.isBlank());
                // ModelAttribute<String> attr = item.getAttribute("name");
                // boolean matched = attr.getValue().toLowerCase().indexOf(q.toLowerCase()) > -1;
                // return empty || matched;
                AtomicInteger count = new AtomicInteger(0);
                this.searchableAttributes.forEach(attrAlias -> {
                    ModelAttribute<String> attr = item.getAttribute(attrAlias);
                    if (attr.getValue().toLowerCase().indexOf(q.toLowerCase()) > -1)
                        count.getAndIncrement();
                });
                return count.get() > 0;
            });
        });

        SortedList<T> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

        TableViewSelectionModel<T> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener(observable -> {
            removeButton.setDisable(selectionModel.getSelectedItem() == null);
            updateButton.setDisable(selectionModel.getSelectedItem() == null);
        });

        removeButton.setDisable(true);
        updateButton.setDisable(true);
    }

    @FXML
    public void onRefreshAction() {
        refresh(true);
    }

    public void refresh(Boolean reattemptConnection) {
        Result<ArrayList<T>> wrapped = this.createEmpty().fetchAllDB();

        if (wrapped.isErr()) {
            if (!DBUtil.isConnected()) {
                if (!reattemptConnection || !DBUtil.getDBConnection().connect(true)) {
                    this.observableList.clear();
                    return;
                }
            }
            wrapped = this.createEmpty().fetchAllDB();
            if (wrapped.isErr())
                FEUtil.showStatusMessage("Error Fetching", wrapped.getError(), true);
        }

        this.observableList.setAll(wrapped.getValue());
    }

    @FXML
    private void onRemoveAction() {
        T selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null)
            selectedItem.deleteFromDB()
                .onSuccess(() -> observableList.remove(selectedItem))
                .onError(m -> FEUtil.showStatusMessage("Error Removing Item", m, true));
    }

    @FXML
    private void onUpdateAction() {
        FEUtil.showUpdateEditor(
            tableView.getSelectionModel().getSelectedItem(),
            "Update " + this.modelItemName
        );
    }

    @FXML
    private void onAddAction() {
        FEUtil.showAddEditor(
            this.createEmpty(),
            "Add " + this.modelItemName,
            this.observableList
        );
    }

    @SuppressWarnings("unchecked")
    private T createEmpty() {
        T newModel = null;

        Constructor<T>[] constructors = (Constructor<T>[]) this.modelClass.getConstructors();
        for (Constructor<T> c : constructors) {
            if (c.getParameterCount() == 0) {
                try {
                    newModel = c.newInstance();
                    break;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return newModel;
    }

    protected ObservableList<T> getModel() {
        return this.observableList;
    }
}
