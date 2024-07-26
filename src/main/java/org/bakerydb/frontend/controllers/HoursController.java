package org.bakerydb.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
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
    private void onAddAction() {
        // Get the current timestamp for clockedIn
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());

        // Create a new Hours object with default values and the current timestamp
        Hours newHours = new Hours(null, null, currentTimestamp, null);

        // Show the clock-in editor dialog
        FEUtil.showAddEditor(
            newHours,
            "Clock In",
            this.getObservableList()
        );
    }
}
