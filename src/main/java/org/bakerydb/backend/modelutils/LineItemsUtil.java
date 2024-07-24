package org.bakerydb.backend.modelutils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bakerydb.backend.DBConnection;
import org.bakerydb.backend.DBManager;
import org.bakerydb.backend.models.LineItems;
import org.bakerydb.util.ErrorMessage;
import org.bakerydb.util.Result;

public final class LineItemsUtil {

    private LineItemsUtil() {
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
     * Add a new line item to LineItems
     * @param item the LineItems to add
     * @return a Result containing either the resulting key or an error message
     */
    public static Result<Integer> newLineItem(LineItems item) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "INSERT INTO LineItems (dishId, orderId, quantity, price, specialinstructions) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = prepareStatement(query, item.getdishId(), item.getorderId(), item.getQuantity(), item.getprice(), item.getspecialInstructions());
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
     * Remove an item from the LineItems
     * @param dishId the dish ID
     * @param orderId the order ID
     * @return a Result containing either a Void or an error message
     */
    public static Result<Void> deleteLineItem(Integer dishId, Integer orderId) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "DELETE FROM LineItems WHERE dishId = ? AND orderId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, dishId, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    /**
     * Update a line item in LineItems
     * @param lineitems the LineItems to update
     * @return a Result containing either a Void or an error message
     */
    public static Result<Void> update(LineItems lineitems) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "UPDATE LineItems SET dishId = ?, orderId = ?, quantity = ?, price = ?, specialinstructions = ? WHERE dishId = ? AND orderId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, lineitems.getdishId(), lineitems.getorderId(), lineitems.getQuantity(), lineitems.getprice(), lineitems.getspecialInstructions(), lineitems.getdishId(), lineitems.getorderId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    /**
     * Fetch all items from LineItems
     * @return a Result containing either an ArrayList of LineItems or an error message
     */
    public static Result<ArrayList<LineItems>> fetchAll() {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM LineItems";
        try {
            PreparedStatement stmt = prepareStatement(query);
            return Result.ok(LineItems.list(stmt.executeQuery()));
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }
    
    /**
     * Fetch a single item from LineItems
     * @param dishId the dish ID
     * @param orderId the order ID
     * @return a Result containing either a LineItems item or an error message
     */
    public static Result<LineItems> fetch(Integer dishId, Integer orderId) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM LineItems WHERE dishId = ? AND orderId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, dishId, orderId);
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
