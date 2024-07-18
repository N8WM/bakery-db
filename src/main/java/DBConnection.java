import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {

    public final Connection connection;

    public DBConnection(String dbName) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("MySQL JDBC not found");
        }

        Dotenv dotenv = Dotenv.load();
        String timeoutMs = "2000";

        String url = String.format(
            "jdbc:mysql://%s:%s/%s?user=%s&password=%s&connectTimeout=%s",
            dotenv.get("HOSTNAME"),
            dotenv.get("PORT"),
            dbName,
            dotenv.get("USERNAME"),
            dotenv.get("PASSWORD"),
            timeoutMs);

        this.connection = DriverManager.getConnection(url);
    }
}
