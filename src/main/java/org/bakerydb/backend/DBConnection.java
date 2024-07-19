package org.bakerydb.backend;

import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

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

        Dotenv dotenv = null;

        try {
            dotenv = Dotenv.configure()
                .filename(".env")
                .load();
        } catch (DotenvException e) {
            System.out.println(e.getLocalizedMessage());
        }

        String timeoutMs = "5000";

        this.hostname = dotenv == null ? "" : dotenv.get("HOSTNAME");
        this.port = dotenv == null ? "" : dotenv.get("PORT");
        this.username = dotenv == null ? "" : dotenv.get("USERNAME");
        this.dbName = dotenv == null ? "" : dotenv.get("DATABASE");
        this.password = dotenv == null ? "" : dotenv.get("PASSWORD");

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
