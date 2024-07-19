package org.bakerydb.backend;

import java.sql.SQLException;

import org.bakerydb.backend.itemutils.InventoryUtil;

public class DBUtil {
    private DBConnection DB;
    public final InventoryUtil inventoryUtil;

    public DBUtil() {
        try {
            this.DB = new DBConnection();
        } catch (SQLException e) {
            this.DB = null;
        }

        this.inventoryUtil = new InventoryUtil(this.DB);
    }

    public boolean isConnected() {
        return this.DB != null;
    }
}
