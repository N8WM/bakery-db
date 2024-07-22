package org.bakerydb.backend.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderUtil {

    // Method to add an order to the database
    public void addOrderItem(OrderItem item) {
        String sql = "INSERT INTO Orders (ccn, date, emplId, total) VALUES (?, ?, ?, ?)";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getCcn());
            statement.setDate(2, new java.sql.Date(item.getDate().getTime()));
            statement.setInt(3, item.getEmplId());
            statement.setFloat(4, item.getTotal());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all orders from the database
    public List<OrderItem> getAllOrderItems() {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT * FROM Orders";
        try (Connection connection = JDBCUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            items = OrderItem.list(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Method to update an order in the database
    public void updateOrderItem(OrderItem item) {
        String sql = "UPDATE Orders SET ccn = ?, date = ?, emplId = ?, total = ? WHERE orderId = ?";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getCcn());
            statement.setDate(2, new java.sql.Date(item.getDate().getTime()));
            statement.setInt(3, item.getEmplId());
            statement.setFloat(4, item.getTotal());
            statement.setInt(5, item.getOrderId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete an order from the database
    public void deleteOrderItem(int orderId) {
        String sql = "DELETE FROM Orders WHERE orderId = ?";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a single order by ID
    public OrderItem getOrderItemById(int orderId) {
        String sql = "SELECT * FROM Orders WHERE orderId = ?";
        OrderItem item = null;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                item = new OrderItem(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }
}
