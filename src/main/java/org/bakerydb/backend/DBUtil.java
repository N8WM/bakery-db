package org.bakerydb.backend;

public class DBUtil {
    private static DBConnection DB;
    private static boolean isSetUp = false;

    private static void ensureSetUp() {
        if (!isSetUp) {
            DB = new DBConnection();
        }

        isSetUp = true;
    }

    public static DBConnection getDBConnection() {
        ensureSetUp();
        return DB;
    }

    public static boolean isConnected() {
        ensureSetUp();
        return DB.isConnected();
    }
}
