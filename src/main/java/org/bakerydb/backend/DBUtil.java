package org.bakerydb.backend;

import org.bakerydb.backend.modelutils.InventoryUtil;

public class DBUtil {
    private static DBConnection DB;
    private static InventoryUtil inventoryUtil;
    private static boolean isSetUp = false;

    private static void ensureSetUp() {
        if (!isSetUp) {
            DB = new DBConnection();
            inventoryUtil = new InventoryUtil(DB);
        }

        isSetUp = true;
    }

    public static InventoryUtil getInventoryUtil() {
        ensureSetUp();
        return inventoryUtil;
    }

    public static boolean isConnected() {
        ensureSetUp();
        return DB.isConnected();
    }
}
