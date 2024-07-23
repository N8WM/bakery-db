package org.bakerydb.backend.modelutils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bakerydb.backend.DBConnection;
import org.bakerydb.backend.models.LineItems;
import org.bakerydb.util.ErrorMessage;
import org.bakerydb.util.Result;

public class LineItemsUtil {
    DBConnection DB;

    public LineItemsUtil(DBConnection DB) {
        this.DB = DB;
    }

    /**
     * Add a new employee to Employees
    * @param item the EmployeesItems to add
     * @return a Result containing either the resulting key or an error message
     */
    public Result<Integer> newLineItems(LineItems item) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "INSERT INTO LineItems (dishId, orderId, quantity, price, specialinstructions) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, item.getdishId());
            stmt.setInt(2, item.getorderId());
            stmt.setInt(3, item.getQuantity());
            stmt.setFloat(4, item.getprice());
            stmt.setString(5, item.getspecialInstructions());

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
    public Result<Void> deleteLineItem(Integer dishId, Integer orderId) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "DELETE FROM Line WHERE dishId = ?, orderId = ?";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            stmt.setInt(1, dishId);
            stmt.setInt(2, orderId);
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
    public Result<Void> update(LineItems lineitems) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "UPDATE Lineitems SET dishId = ?, orderId = ?, quantity = ?, price = ?, specialinstructions = ?";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            stmt.setInt(1, lineitems.getdishId());
            stmt.setInt(2, lineitems.getorderId());
            stmt.setInt(3, lineitems.getQuantity());
            stmt.setFloat(3, lineitems.getprice());
            stmt.setString(4, lineitems.getspecialInstructions());
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
    public Result<ArrayList<LineItems>> fetchAll() {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM LineItems";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            return Result.ok(LineItems.list(stmt.executeQuery()));
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }
    
    /**
     * Fetch a single item from the inventory
     * @param invId the id of the inventory item
     * @return a Result containing either an Inventory item or an error message
     */
    public Result<LineItems> fetch(Integer dishId, Integer orderId) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM LineItems WHERE dishId = ?, AND orderId = ?";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            stmt.setInt(1, dishId);
            stmt.setInt(2, orderId);
            ResultSet result = stmt.executeQuery();
            if (result.next())
                return Result.ok(new LineItems(result));
            else
                return Result.err(ErrorMessage.ITEM_NOT_FOUND);
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }
}