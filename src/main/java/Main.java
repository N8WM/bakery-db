import java.sql.*;

class Main {

    public static void main(String[] args) {

        try {
            DBConnection DB = new DBConnection("bakerydb");
            System.out.println("Connection established");
            app();
        } catch (Exception e) {
            System.out.println("Unable to connect");
        }
    }

    public static void app() { }
}
