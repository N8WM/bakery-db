// package org.bakerydb.backend.modelutils;
//
// import org.bakerydb.util.*;
// import org.bakerydb.backend.DBConnection;
// import org.bakerydb.backend.models.InventoryItem;
// import java.util.ArrayList;
// import java.sql.*;
//
// public class InventoryUtil {
//     DBConnection DB;
//
//     public InventoryUtil(DBConnection DB) {
//         this.DB = DB;
//     }
//
//     /**
//      * Add a new item to the inventory
//     * @param item the InventoryItem to add
//      * @return a Result containing either the resulting key or an error message
//      */
//     public Result<Integer> newItem(InventoryItem item) {
//         if (!this.DB.isConnected())
//             return Result.err(ErrorMessage.NO_CONNECTION);
//
//         String query = "INSERT INTO Inventory (name, quantity, unit, reorderLevel) VALUES (?, ?, ?, ?)";
//         try {
//             PreparedStatement stmt = this.DB.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
//             stmt.setString(1, item.getName());
//             stmt.setFloat(2, item.getQuantity());
//             stmt.setString(3, item.getUnit());
//             stmt.setFloat(4, item.getReorderLevel());
//
//             int affectedRows = stmt.executeUpdate();
//             if (affectedRows > 0) {
//                 ResultSet keys = stmt.getGeneratedKeys();
//                 if (keys.next())
//                     return Result.ok(keys.getInt(1));
//                 else
//                     return Result.err(ErrorMessage.NO_KEY);
//             } else {
//                 return Result.err(ErrorMessage.NO_CHANGE);
//             }
//
//         } catch (SQLException e) {
//             return Result.err(e.toString());
//         }
//     }
//
//     /**
//      * Remove an item from the inventory
//      * @param invId the id of the inventory item
//      * @return a Result containing either a Void or an error message
//      */
//     public Result<Void> deleteItem(Integer invId) {
//         if (!this.DB.isConnected())
//             return Result.err(ErrorMessage.NO_CONNECTION);
//
//         String query = "DELETE FROM Inventory WHERE invId = ?";
//         try {
//             PreparedStatement stmt = this.DB.connection.prepareStatement(query);
//             stmt.setInt(1, invId);
//             stmt.executeUpdate();
//         } catch (SQLException e) {
//             return Result.err(e.toString());
//         }
//         return Result.ok();
//     }
//
//     /**
//      * Update the quantity of an item in the inventory
//      * @param invId the id of the inventory item
//      * @param quantity the new quantity of the item
//      * @return a Result containing either a Void or an error message
//      */
//     public Result<Void> setQuantity(Integer invId, Float quantity) {
//         if (!this.DB.isConnected())
//             return Result.err(ErrorMessage.NO_CONNECTION);
//
//         String query = "UPDATE Inventory SET quantity = ? WHERE invId = ?";
//         try {
//             PreparedStatement stmt = this.DB.connection.prepareStatement(query);
//             stmt.setFloat(1, quantity);
//             stmt.setInt(2, invId);
//             stmt.executeUpdate();
//         } catch (SQLException e) {
//             return Result.err(e.toString());
//         }
//         return Result.ok();
//     }
//
//     /**
//      * Update the quantity of an item in the inventory by a given amount
//      * @param invId the id of the inventory item
//      * @param amount the amount to change the quantity by
//      * @return a Result containing either a Void or an error message
//      */
//     public Result<Void> changeQuantityBy(Integer invId, Float amount) {
//         if (!this.DB.isConnected())
//             return Result.err(ErrorMessage.NO_CONNECTION);
//
//         String query = "UPDATE Inventory SET quantity = quantity + ? WHERE invId = ?";
//         try {
//             PreparedStatement stmt = this.DB.connection.prepareStatement(query);
//             stmt.setFloat(1, amount);
//             stmt.setInt(2, invId);
//             stmt.executeUpdate();
//         } catch (SQLException e) {
//             return Result.err(e.toString());
//         }
//         return Result.ok();
//     }
//
//     /**
//      * Update an item in the inventory
//      * @param item the InventoryItem to update
//      * @return a Result containing either a Void or an error message
//      */
//     public Result<Void> update(InventoryItem item) {
//         if (!this.DB.isConnected())
//             return Result.err(ErrorMessage.NO_CONNECTION);
//
//         String query = "UPDATE Inventory SET name = ?, quantity = ?, unit = ?, reorderLevel = ? WHERE invId = ?";
//         try {
//             PreparedStatement stmt = this.DB.connection.prepareStatement(query);
//             stmt.setString(1, item.getName());
//             stmt.setFloat(2, item.getQuantity());
//             stmt.setString(3, item.getUnit());
//             stmt.setFloat(4, item.getReorderLevel());
//             stmt.setInt(5, item.getInvId());
//             stmt.executeUpdate();
//         } catch (SQLException e) {
//             return Result.err(e.toString());
//         }
//         return Result.ok();
//     }
//
//     /**
//      * Fetch all items from the inventory
//      * @return a Result containing either an ArrayList of Inventory items or an error message
//      */
//     public Result<ArrayList<InventoryItem>> fetchAll() {
//         if (!this.DB.isConnected())
//             return Result.err(ErrorMessage.NO_CONNECTION);
//
//         String query = "SELECT * FROM Inventory";
//         try {
//             PreparedStatement stmt = this.DB.connection.prepareStatement(query);
//             return Result.ok(InventoryItem.list(stmt.executeQuery()));
//         } catch (SQLException e) {
//             return Result.err(e.toString());
//         }
//     }
//
//     /**
//      * Fetch a single item from the inventory
//      * @param invId the id of the inventory item
//      * @return a Result containing either an Inventory item or an error message
//      */
//     public Result<InventoryItem> fetch(Integer invId) {
//         if (!this.DB.isConnected())
//             return Result.err(ErrorMessage.NO_CONNECTION);
//
//         String query = "SELECT * FROM Inventory WHERE invId = ?";
//         try {
//             PreparedStatement stmt = this.DB.connection.prepareStatement(query);
//             stmt.setInt(1, invId);
//             ResultSet result = stmt.executeQuery();
//             if (result.next())
//                 return Result.ok(new InventoryItem(result));
//             else
//                 return Result.err(ErrorMessage.ITEM_NOT_FOUND);
//         } catch (SQLException e) {
//             return Result.err(e.toString());
//         }
//     }
// }
