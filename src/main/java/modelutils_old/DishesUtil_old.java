package modelutils_old;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bakerydb.backend.DBConnection;
import models_old.DishesItems_old;
import org.bakerydb.util.ErrorMessage;
import org.bakerydb.util.Result;

public class DishesUtil_old {
    DBConnection DB;

    public DishesUtil_old(DBConnection DB) {
        this.DB = DB;
    }

     /**
     * Add a new dish to dishes
    * @param item the DishesItems to add
     * @return a Result containing either the resulting key or an error message
     */
    public Result<Integer> newItem(DishesItems_old item) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "INSERT INTO Dishes (name, price, category, description) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, item.getName());
            stmt.setFloat(2, item.getprice());
            stmt.setString(3, item.getcategory());
            stmt.setString(4, item.getdescription());

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
    public Result<Void> deleteDish(Integer dishId) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "DELETE FROM Dishes WHERE dishId = ?";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            stmt.setInt(1, dishId);
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
    public Result<Void> update(DishesItems_old dish) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "UPDATE Dishes SET name = ?, price = ?, category = ?, description = ? WHERE dishId = ?";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            stmt.setString(1, dish.getName());
            stmt.setFloat(2, dish.getprice());
            stmt.setString(3, dish.getcategory());
            stmt.setString(4, dish.getdescription());
            stmt.setInt(5, dish.getDishId());
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
    public Result<ArrayList<DishesItems_old>> fetchAll() {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM Dishes";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            return Result.ok(DishesItems_old.list(stmt.executeQuery()));
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

    /**
     * Fetch a single item from the inventory
     * @param invId the id of the inventory item
     * @return a Result containing either an Inventory item or an error message
     */
    public Result<DishesItems_old> fetch(Integer dishId) {
        if (!this.DB.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM Dishes WHERE dishId = ?";
        try {
            PreparedStatement stmt = this.DB.connection.prepareStatement(query);
            stmt.setInt(1, dishId);
            ResultSet result = stmt.executeQuery();
            if (result.next())
                return Result.ok(new DishesItems_old(result));
            else
                return Result.err(ErrorMessage.ITEM_NOT_FOUND);
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }
}
