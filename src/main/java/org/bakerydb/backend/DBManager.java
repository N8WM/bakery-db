package org.bakerydb.backend;

public class DBManager {
    private static DBManager instance;
    private DBConnection DB;
    private boolean isSetUp;

    private DBManager() {
        this.isSetUp = false;
        ensureSetUp();
    }

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private void ensureSetUp() {
        if (!isSetUp) {
            this.DB = new DBConnection();
            isSetUp = true;
        }
    }

    public boolean isConnected() {
        ensureSetUp();
        return DB.isConnected();
    }

    public DBConnection getDBConnection() {
        return this.DB;
    }
}
