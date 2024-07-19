package org.bakerydb.frontend;

import org.bakerydb.backend.*;

class Main {

    public static void main(String[] args) {
        DBUtil dbUtil = new DBUtil();

        System.out.println("Connected: " + dbUtil.isConnected());
    }

    public static void app() {
    }
}
