package org.bakerydb.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.bakerydb.backend.models.Employee;
import org.bakerydb.frontend.FEUtil;

public class EmployeeController extends BaseTabController<Employee> {

    @FXML
    private TableColumn<Employee, Integer> idTableColumn;
    @FXML
    private TableColumn<Employee, String> firstNameTableColumn;
    @FXML
    private TableColumn<Employee, String> middleInitialTableColumn;
    @FXML
    private TableColumn<Employee, String> lastNameTableColumn;
    @FXML
    private TableColumn<Employee, String> roleTableColumn;
    @FXML
    private TableColumn<Employee, Date> dateHiredTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        onInitialize(
            List.of(idTableColumn, firstNameTableColumn, middleInitialTableColumn, lastNameTableColumn, roleTableColumn, dateHiredTableColumn),
            List.of("firstName", "lastName"),
            Employee.class,
            "Employee"
        );
    }

    @FXML
    private void onAddAction() {
        Employee newEmployee = new Employee(null, "", "", "", "", Date.valueOf(LocalDate.now()));

        FEUtil.showAddEditor(
            newEmployee,
            "Add Employee",
            this.getModel()
        );
    }
}
