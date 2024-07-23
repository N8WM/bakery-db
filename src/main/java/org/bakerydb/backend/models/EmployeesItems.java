package org.bakerydb.backend.models;

import java.sql.*;
import java.util.ArrayList;

import javafx.beans.property.*;

public class EmployeesItems {
    private IntegerProperty emplId;
    private StringProperty firstName;
    private StringProperty middleInitial;
    private StringProperty lastName;
    private StringProperty role;
    private StringProperty dateHired;

    public EmployeesItems(
        Integer emplId,
        String firstName, 
        String middleInitial, 
        String lastName, 
        String role, 
        String dateHired) 
        {
        this.emplId = new SimpleIntegerProperty(emplId);
        this.firstName = new SimpleStringProperty(firstName);
        this.middleInitial = new SimpleStringProperty(middleInitial);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
        this.dateHired = new SimpleStringProperty(dateHired);
    }

    public EmployeesItems(ResultSet result) throws SQLException {
        this(
            result.getInt("emplId"),
            result.getString("firstName"),
            result.getString("middleInitial"),
            result.getString("lastName"),
            result.getString("role"),
            result.getString("dateHired")
        );
    }

    public EmployeesItems() {
        this.emplId = new SimpleIntegerProperty();
        this.firstName = new SimpleStringProperty();
        this.middleInitial = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.dateHired = new SimpleStringProperty();
    }

    public static ArrayList<EmployeesItems> list(ResultSet result) throws SQLException {
        ArrayList<EmployeesItems> employeesList = new ArrayList<EmployeesItems>();
        while (result.next()) {
            employeesList.add(new EmployeesItems(result));
        }
        return employeesList;
    }

    public EmployeesItems clone() {
        return new EmployeesItems(
            this.emplId.get(),
            this.firstName.get(),
            this.middleInitial.get(),
            this.lastName.get(),
            this.role.get(),
            this.dateHired.get()
        );
    }

    public void update(EmployeesItems other) {
        this.emplId.set(other.getemplId());
        this.firstName.set(other.getFirstName());
        this.middleInitial.set(other.getmiddleInitial());
        this.lastName.set(other.getLastName());
        this.role.set(other.getRole());
        this.dateHired.set(other.getDateHired());
    }

    public Integer getemplId() {
        return this.emplId.get();
    }

    public IntegerProperty emplIdProperty() {
        return this.emplId;
    }

    public void setemplId(Integer emplId) {
        this.emplId.set(emplId);
    }
////////////////////////////////////////////////////
    public String getFirstName() {
        return this.firstName.get();
    }

    public StringProperty firstnameProperty() {
        return this.firstName;
    }

    public void setfirstName(String firstName) {
        this.firstName.set(firstName);
    }

////////////////////////////////////////////////////
public String getmiddleInitial() {
    return this.middleInitial.get();
}

public StringProperty middleInitialProperty() {
    return this.middleInitial;
}

public void setName(String middleInitial) {
    this.middleInitial.set(middleInitial);
}
////////////////////////////////////////////////////
public String getLastName() {
    return this.lastName.get();
}

public StringProperty lastNameProperty() {
    return this.lastName;
}

public void setlastName(String lastName) {
    this.lastName.set(lastName);
}

////////////////////////////////////////////////////
    public String getRole() {
        return this.role.get();
    }

    public StringProperty roleProperty() {
        return this.role;
    }

    public void setrole(String role) {
        this.role.set(role);
    }
///////////////////////////////////////////////////
    public String getDateHired() {
        return this.dateHired.get();
    }

    public StringProperty dateHiredProperty() {
        return this.dateHired;
    }

    public void setdateHired(String dateHired) {
        this.dateHired.set(dateHired);
    }
}
