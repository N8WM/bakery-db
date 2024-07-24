package org.bakerydb.backend.modelutils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bakerydb.backend.DBConnection;
import org.bakerydb.backend.DBManager;
import org.bakerydb.backend.models.InventoryItem;
import org.bakerydb.util.ErrorMessage;
import org.bakerydb.util.Result;

public final class InventoryUtil {

    private InventoryUtil() {
        throw new UnsupportedOperationException("Util class");
    }

    private static PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        DBConnection DB = DBManager.getInstance().getDBConnection();
        PreparedStatement stmt = DB.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }

    /**
     * Add a new item to the inventory
     * @param item the InventoryItem to add
     * @return a Result containing either the resulting key or an error message
     */
    public static Result<Integer> newItem(InventoryItem item) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "INSERT INTO Inventory (name, quantity, unit, reorderLevel) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = prepareStatement(query, item.getName(), item.getQuantity(), item.getUnit(), item.getReorderLevel());
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
     * Remove an item from the inventory
     * @param invId the id of the inventory item
     * @return a Result containing either a Void or an error message
     */
    public static Result<Void> deleteItem(Integer invId) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "DELETE FROM Inventory WHERE invId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, invId);
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
    public static Result<Void> setQuantity(Integer invId, Float quantity) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "UPDATE Inventory SET quantity = ? WHERE invId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, quantity, invId);
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
    public static Result<Void> changeQuantityBy(Integer invId, Float amount) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "UPDATE Inventory SET quantity = quantity + ? WHERE invId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, amount, invId);
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
    public static Result<Void> update(InventoryItem item) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "UPDATE Inventory SET name = ?, quantity = ?, unit = ?, reorderLevel = ? WHERE invId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, item.getName(), item.getQuantity(), item.getUnit(), item.getReorderLevel(), item.getInvId());
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
    public static Result<ArrayList<InventoryItem>> fetchAll() {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM Inventory";
        try {
            PreparedStatement stmt = prepareStatement(query);
            return Result.ok(InventoryItem.list(stmt.executeQuery()));
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

    /**
     * Fetch a single item from the inventory
     * @param invId the id of the inventory item
     * @return a Result containing either an Inventory item or an error message
     */
    public static Result<InventoryItem> fetch(Integer invId) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM Inventory WHERE invId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, invId);
            ResultSet result = stmt.executeQuery();
            if (result.next())
                return Result.ok(new InventoryItem(result));
            else
                return Result.err(ErrorMessage.ITEM_NOT_FOUND);
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }
}
