package org.bakerydb.backend.items;

import java.sql.*;
import java.util.ArrayList;

public class Inventory {
    public final Integer invId;
    public final String name;
    public final Integer quantity;
    public final String unit;
    public final Integer reorderLevel;

    public Inventory(ResultSet result) throws SQLException {
        this.invId = result.getInt("invId");
        this.name = result.getString("name");
        this.quantity = result.getInt("quantity");
        this.unit = result.getString("unit");
        this.reorderLevel = result.getInt("reorderLevel");
    }

    public static ArrayList<Inventory> list(ResultSet result) throws SQLException {
        ArrayList<Inventory> inventoryList = new ArrayList<Inventory>();
        while (result.next()) {
            inventoryList.add(new Inventory(result));
        }
        return inventoryList;
    }
}
