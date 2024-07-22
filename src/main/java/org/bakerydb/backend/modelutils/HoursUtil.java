package org.bakerydb.backend.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoursUtil {

    // Method to add a new hours record to the database
    public void addHoursItem(HoursItem item) {
        String sql = "INSERT INTO Hours (emplId, clockedIn, clockedOut) VALUES (?, ?, ?)";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, item.getEmplId());
            statement.setTimestamp(2, new Timestamp(item.getClockedIn().getTime()));
            statement.setTimestamp(3, item.getClockedOut() != null ? new Timestamp(item.getClockedOut().getTime()) : null);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all hours records from the database
    public List<HoursItem> getAllHoursItems() {
        List<HoursItem> items = new ArrayList<>();
        String sql = "SELECT * FROM Hours";
        try (Connection connection = JDBCUtils.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            items = HoursItem.list(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // Method to update an hours record in the database
    public void updateHoursItem(HoursItem item) {
        String sql = "UPDATE Hours SET emplId = ?, clockedIn = ?, clockedOut = ? WHERE hid = ?";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, item.getEmplId());
            statement.setTimestamp(2, new Timestamp(item.getClockedIn().getTime()));
            statement.setTimestamp(3, item.getClockedOut() != null ? new Timestamp(item.getClockedOut().getTime()) : null);
            statement.setInt(4, item.getHid());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete an hours record from the database
    public void deleteHoursItem(int hid) {
        String sql = "DELETE FROM Hours WHERE hid = ?";
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, hid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a single hours record by ID
    public HoursItem getHoursItemById(int hid) {
        String sql = "SELECT * FROM Hours WHERE hid = ?";
        HoursItem item = null;
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, hid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                item = new HoursItem(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }
}
