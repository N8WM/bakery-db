import java.sql.*;

class Main {

    public static void main(String[] args) {

        Connection connect = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?user=root&password=password");
            System.out.println("Connection established");
            System.out.println(connect.getCatalog());
        } catch (Exception e) {
            System.out.println("Unable to connect");
        }
    }
}
