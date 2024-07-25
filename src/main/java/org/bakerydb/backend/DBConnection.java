package org.bakerydb.backend;

import java.sql.*;

import org.bakerydb.frontend.FEUtil;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class DBConnection {

    public Connection connection;
    public final String hostname;
    public final String port;
    public final String username;
    public final String dbName;
    private final String password;

    public DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("MySQL JDBC not found");
            this.hostname = this.port = this.username = this.dbName = this.password = "";
            this.connection = null;
            return;
        }

        Dotenv dotenv;

        try {
            dotenv = Dotenv.configure()
                .filename(".env")
                .load();
        } catch (DotenvException e) {
            System.out.println(e.getLocalizedMessage());
            this.hostname = this.port = this.username = this.dbName = this.password = "";
            this.connection = null;
            return;
        }


        this.hostname = dotenv.get("HOSTNAME");
        this.port = dotenv.get("PORT");
        this.username = dotenv.get("USERNAME");
        this.dbName = dotenv.get("DATABASE");
        this.password = dotenv.get("PASSWORD");

        this.connect(true);
    }

    public boolean connect(boolean showStatusMessage) {
        String timeoutMs = "5000";

        String url = String.format(
            "jdbc:mysql://%s:%s/%s?user=%s&password=%s&connectTimeout=%s",
            this.hostname,
            this.port,
            this.dbName,
            this.username,
            this.password,
            timeoutMs
        );

        Connection temp;

        try {
            temp = DriverManager.getConnection(url);
        } catch (SQLException e) {
            temp = null;
        }

        this.connection = temp;

        if (!this.isConnected() && showStatusMessage)
            FEUtil.showStatusMessage(
                "Connection Error",
                "Could not connect to the BakeryDB database. "
                + "Ensure you have a stable connection to the Cal Poly campus network.",
                true
            );

        return this.connection != null;
    }

    public boolean isConnected() {
        return this.connection != null;
    }
}
