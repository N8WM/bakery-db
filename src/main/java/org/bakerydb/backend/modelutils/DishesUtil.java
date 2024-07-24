package org.bakerydb.backend.modelutils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bakerydb.backend.DBConnection;
import org.bakerydb.backend.models.DishesItems;
import org.bakerydb.util.ErrorMessage;
import org.bakerydb.util.Result;
import org.bakerydb.backend.DBManager;

public final class DishesUtil {

    private DishesUtil() {
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
     * Add a new dish to dishes
     * @param item the DishesItems to add
     * @return a Result containing either the resulting key or an error message
     */
    public static Result<Integer> newItem(DishesItems item) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "INSERT INTO Dishes (name, price, category, description) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = prepareStatement(query, item.getName(), item.getprice(), item.getcategory(), item.getdescription());
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
     * Remove an item from the Dishes
     * @param dishId the id of the inventory item
     * @return a Result containing either a Void or an error message
     */
    public static Result<Void> deleteDish(Integer dishId) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "DELETE FROM Dishes WHERE dishId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, dishId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    /**
     * Update an item in the inventory
     * @param item the DishesItems to update
     * @return a Result containing either a Void or an error message
     */
    public static Result<Void> update(DishesItems dish) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "UPDATE Dishes SET name = ?, price = ?, category = ?, description = ? WHERE dishId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, dish.getName(), dish.getprice(), dish.getcategory(), dish.getdescription(), dish.getDishId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    /**
     * Fetch all items from the inventory
     * @return a Result containing either an ArrayList of DishesItems or an error message
     */
    public static Result<ArrayList<DishesItems>> fetchAll() {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM Dishes";
        try {
            PreparedStatement stmt = prepareStatement(query);
            return Result.ok(DishesItems.list(stmt.executeQuery()));
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

    /**
     * Fetch a single item from the inventory
     * @param dishId the id of the inventory item
     * @return a Result containing either a DishesItems item or an error message
     */
    public static Result<DishesItems> fetch(Integer dishId) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM Dishes WHERE dishId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, dishId);
            ResultSet result = stmt.executeQuery();
            if (result.next())
                return Result.ok(new DishesItems(result));
            else
                return Result.err(ErrorMessage.ITEM_NOT_FOUND);
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }
}
