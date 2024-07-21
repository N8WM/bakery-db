package org.bakerydb.backend;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DBSetup {
    public static void run() {
        java.net.URL tables = Thread.currentThread()
            .getContextClassLoader()
            .getResource("tables.sql");

        if (tables == null) {
            System.out.println("`tables.sql` not found");
            return;
        }

        String query = null;

        try {
            query = new String(tables.openStream().readAllBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ArrayList<String> queries = new ArrayList<>(Arrays.asList(query.split("\\s*;\\s*")));

        try {
            DBConnection DB = new DBConnection();
            Statement stmt = DB.connection.createStatement();
            queries.stream()
                .filter(q -> !q.isBlank())
                .forEach(q -> {
                    try {
                        stmt.addBatch(q);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                });
                
            stmt.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
