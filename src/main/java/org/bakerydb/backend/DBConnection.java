package org.bakerydb.backend;

import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {

    public final Connection connection;
    public final String hostname;
    public final String port;
    public final String username;
    public final String dbName;
    private final String password;

    public DBConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("MySQL JDBC not found");
        }

        Dotenv dotenv = Dotenv.load();
        String timeoutMs = "2000";

        this.hostname = dotenv.get("HOSTNAME");
        this.port = dotenv.get("PORT");
        this.username = dotenv.get("USERNAME");
        this.dbName = dotenv.get("DATABASE");
        this.password = dotenv.get("PASSWORD");

        String url = String.format(
                "jdbc:mysql://%s:%s/%s?user=%s&password=%s&connectTimeout=%s",
                this.hostname,
                this.port,
                this.dbName,
                this.username,
                this.password,
                timeoutMs);

        this.connection = DriverManager.getConnection(url);
    }
}
