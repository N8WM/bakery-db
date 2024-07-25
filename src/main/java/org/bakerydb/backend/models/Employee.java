package org.bakerydb.backend.models;

import java.sql.*;
import java.util.Date;

import org.bakerydb.util.Model;
import org.bakerydb.util.ModelAttribute;

import javafx.util.converter.*;

public class Employee extends Model<Employee> {
    private ModelAttribute<Integer> emplId;
    private ModelAttribute<String> firstName;
    private ModelAttribute<String> middleInitial;
    private ModelAttribute<String> lastName;
    private ModelAttribute<String> role;
    private ModelAttribute<Date> dateHired;

    public Employee(Integer emplId, String firstName, String middleInitial, String lastName, String role, Date dateHired) {
        super("Employees",
            new ModelAttribute<Integer>(emplId, "emplId", Integer.class)
                .setDisplayName("ID")
                .setConverter(IntegerStringConverter.class)
                .setUserEditable(false)
                .setKey(true),
            new ModelAttribute<String>(firstName, "firstName", String.class)
                .setDisplayName("First Name"),
            new ModelAttribute<String>(middleInitial, "middleInitial", String.class)
                .setDisplayName("Middle Initial"),
            new ModelAttribute<String>(lastName, "lastName", String.class)
                .setDisplayName("Last Name"),
            new ModelAttribute<String>(role, "role", String.class)
                .setDisplayName("Role"),
            new ModelAttribute<Date>(dateHired, "dateHired", Date.class)
                .setDisplayName("Date Hired")
                .setConverter(DateStringConverter.class)
        );

        this.emplId = this.getAttribute("emplId");
        this.firstName = this.getAttribute("firstName");
        this.middleInitial = this.getAttribute("middleInitial");
        this.lastName = this.getAttribute("lastName");
        this.role = this.getAttribute("role");
        this.dateHired = this.getAttribute("dateHired");
    }

    public Employee() {
        this(null, null, null, null, null, null);
    }

    public Employee(ResultSet result) throws SQLException {
        this();
        this.updateFromSQL(result);
    }

    @Override
    public Employee clone() {
        return new Employee(
            this.emplId.getValue(),
            this.firstName.getValue(),
            this.middleInitial.getValue(),
            this.lastName.getValue(),
            this.role.getValue(),
            this.dateHired.getValue()
        );
    }
}
