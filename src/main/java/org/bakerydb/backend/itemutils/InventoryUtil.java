package org.bakerydb.backend.itemutils;

import org.bakerydb.util.*;
import org.bakerydb.backend.DBConnection;
import org.bakerydb.backend.items.Inventory;
import java.util.ArrayList;
import java.sql.*;

public class InventoryUtil {
    DBConnection DB;

    public InventoryUtil(DBConnection DB) { }

    /**
     * Add a new item to the inventory
     * @param name the name of the item
     * @param quantity the quantity of the item in stock
     * @param unit the unit associated with the quatity
     * @param reorderLevel the level at which the item should be reordered
     * @return a Result containing either a Void or an error message
     */
    public Result<Void> newItem(
        String name,
        Integer quantity,
        String unit,
        Integer reorderLevel
    ) {
        String query = "INSERT INTO Inventory (name, quantity, unit, reorderLevel) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = DB.connection.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, quantity);
            stmt.setString(3, unit);
            stmt.setInt(4, reorderLevel);
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    /**
     * Remove an item from the inventory
     * @param invId the id of the inventory item
     * @return a Result containing either a Void or an error message
     */
    public Result<Void> deleteItem(Integer invId) {
        String query = "DELETE FROM Inventory WHERE invId = ?";
        try {
            PreparedStatement stmt = DB.connection.prepareStatement(query);
            stmt.setInt(1, invId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    /**
     * Update the quantity of an item in the inventory
     * @param invId the id of the inventory item
     * @param quantity the new quantity of the item
     * @return a Result containing either a Void or an error message
     */
    public Result<Void> setQuantity(Integer invId, Integer quantity) {
        String query = "UPDATE Inventory SET quantity = ? WHERE invId = ?";
        try {
            PreparedStatement stmt = DB.connection.prepareStatement(query);
            stmt.setInt(1, quantity);
            stmt.setInt(2, invId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    /**
     * Update the quantity of an item in the inventory by a given amount
     * @param invId the id of the inventory item
     * @param amount the amount to change the quantity by
     * @return a Result containing either a Void or an error message
     */
    public Result<Void> changeQuantityBy(Integer invId, Integer amount) {
        String query = "UPDATE Inventory SET quantity = quantity + ? WHERE invId = ?";
        try {
            PreparedStatement stmt = DB.connection.prepareStatement(query);
            stmt.setInt(1, amount);
            stmt.setInt(2, invId);
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
    public Result<ArrayList<Inventory>> fetchAll() {
        String query = "SELECT * FROM Inventory";
        try {
            PreparedStatement stmt = DB.connection.prepareStatement(query);
            return Result.ok(Inventory.list(stmt.executeQuery()));
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

    /**
     * Fetch a single item from the inventory
     * @param invId the id of the inventory item
     * @return a Result containing either an Inventory item or an error message
     */
    public Result<Inventory> fetch(Integer invId) {
        String query = "SELECT * FROM Inventory WHERE invId = ?";
        try {
            PreparedStatement stmt = DB.connection.prepareStatement(query);
            stmt.setInt(1, invId);
            ResultSet result = stmt.executeQuery();
            if (result.next())
                return Result.ok(new Inventory(result));
            else
                return Result.err(ErrorMessage.ITEM_NOT_FOUND);
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }
}
