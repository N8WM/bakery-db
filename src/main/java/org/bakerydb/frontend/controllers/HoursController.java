package org.bakerydb.frontend.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.Button;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import org.bakerydb.backend.models.Hours;
import org.bakerydb.frontend.FEUtil;

public class HoursController extends BaseTabController<Hours> {

    @FXML
    private TableColumn<Hours, Integer> idTableColumn;
    @FXML
    private TableColumn<Hours, Integer> emplIdTableColumn;
    @FXML
    private TableColumn<Hours, String> firstNameTableColumn;
    @FXML
    private TableColumn<Hours, String> lastNameTableColumn;
    @FXML
    private TableColumn<Hours, Timestamp> timeInColumn;
    @FXML
    private TableColumn<Hours, Timestamp> timeOutColumn;
    @FXML
    private Button clockOutButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onInitialize(
            List.of(idTableColumn, emplIdTableColumn, firstNameTableColumn, lastNameTableColumn, timeInColumn, timeOutColumn),
            List.of("firstName", "lastName"),
            Hours.class,
            "Timesheet"
        );

        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("hid"));
        emplIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("emplId"));
        firstNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        timeInColumn.setCellValueFactory(new PropertyValueFactory<>("clockedIn"));
        timeOutColumn.setCellValueFactory(new PropertyValueFactory<>("clockedOut"));

        TableViewSelectionModel<Hours> selectionModel = tableView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean isClockOutEnabled = newValue != null && newValue.getClockedOut() == null;
            if (clockOutButton != null) {
                clockOutButton.setDisable(!isClockOutEnabled);
            }
        });
    }

    @FXML
    private void onClockInAction() {
        Hours newHours = new Hours(null, null, Timestamp.valueOf(LocalDateTime.now()), null);
        ObservableList<Hours> toAdd = this.getObservableList();

        FEUtil.showClockInEditor(newHours, "Clock In", toAdd);
    }

    @FXML
    private void onClockOutAction() {
        Hours selectedHours = tableView.getSelectionModel().getSelectedItem();
        if (selectedHours != null && selectedHours.getClockedOut() == null) {
            selectedHours.setClockedOut(Timestamp.valueOf(LocalDateTime.now()));
            selectedHours.updateDB()
                .onSuccess(() -> tableView.refresh())
                .onError(m -> FEUtil.showStatusMessage("Failed to Clock Out", m, true));
        }
    }
}
