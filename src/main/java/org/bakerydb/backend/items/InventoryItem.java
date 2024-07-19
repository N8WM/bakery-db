package org.bakerydb.backend.items;

import java.sql.*;
import java.util.ArrayList;

public class InventoryItem {
    public final Integer invId;
    public final String name;
    public final Float quantity;
    public final String unit;
    public final Float reorderLevel;

    public InventoryItem(ResultSet result) throws SQLException {
        this.invId = result.getInt("invId");
        this.name = result.getString("name");
        this.quantity = result.getFloat("quantity");
        this.unit = result.getString("unit");
        this.reorderLevel = result.getFloat("reorderLevel");
    }

    public static ArrayList<InventoryItem> list(ResultSet result) throws SQLException {
        ArrayList<InventoryItem> inventoryList = new ArrayList<InventoryItem>();
        while (result.next()) {
            inventoryList.add(new InventoryItem(result));
        }
        return inventoryList;
    }
}
