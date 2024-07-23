package org.bakerydb.backend.modelutils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bakerydb.backend.DBConnection;
import org.bakerydb.backend.models.EmployeesItems;
import org.bakerydb.util.ErrorMessage;
import org.bakerydb.util.Result;

public class EmployeesUtil {
    DBConnection DB;

    public EmployeesUtil(DBConnection DB) {
        this.DB = DB;
    }

    /**
     * Add a new employee to Employees
    * @param item the EmployeesItems to add
     * @return a Result containing either the resulting key or an error message
     */
    public Result<Integer> newEmployee(EmployeesItems item) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "INSERT INTO Employees (firstName, middleInitial, lastName, role, dateHired) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, item.getFirstName());
            stmt.setString(2, item.getmiddleInitial());
            stmt.setString(3, item.getLastName());
            stmt.setString(4, item.getRole());
            stmt.setString(5, item.getDateHired());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next())
                    return Result.ok(keys.getInt(1));
                else
                    return Result.err(ErrorMessage.NO_KEY);
            } else {
                return Result.err(ErrorMessage.NO_CHANGE);
            }
            
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

    /**
     * Remove an item from the Employees
     * @param emplId the id of the inventory item
     * @return a Result containing either a Void or an error message
     */
    public Result<Void> deleteEmployee(Integer emplId) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "DELETE FROM Employees WHERE emplId = ?";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            stmt.setInt(1, emplId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    /**
     * Update an item in the inventory
     * @param item the InventoryItem to update
     * @return a Result containing either a Void or an error message
     */
    public Result<Void> update(EmployeesItems employee) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "UPDATE Employees SET firstName = ?, middleInitial = ?, lastName = ?, role = ?, dateHired = ?";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getmiddleInitial());
            stmt.setString(3, employee.getLastName());
            stmt.setString(3, employee.getRole());
            stmt.setString(4, employee.getDateHired());
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    
    /**
     * Fetch all items from the inventory
     * @return a Result containing either an ArrayList of Inventory items or an error message
     */
    public Result<ArrayList<EmployeesItems>> fetchAll() {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM Employees";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            return Result.ok(EmployeesItems.list(stmt.executeQuery()));
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

        /**
     * Fetch a single item from the inventory
     * @param invId the id of the inventory item
     * @return a Result containing either an Inventory item or an error message
     */
    public Result<EmployeesItems> fetch(Integer emplId) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM Employees WHERE emplId = ?";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            stmt.setInt(1, emplId);
            ResultSet result = stmt.executeQuery();
            if (result.next())
                return Result.ok(new EmployeesItems(result));
            else
                return Result.err(ErrorMessage.ITEM_NOT_FOUND);
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }
}
