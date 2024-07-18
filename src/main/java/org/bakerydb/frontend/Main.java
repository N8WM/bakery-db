package org.bakerydb.frontend;

import org.bakerydb.backend.*;

class Main {

    public static void main(String[] args) {

        try {
            DBConnection DB = new DBConnection();
            System.out.println("Connection established");
            app();
        } catch (Exception e) {
            System.out.println("Unable to connect");
        }
    }

    public static void app() {
    }
}
