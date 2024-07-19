package org.bakerydb.frontend;

import org.bakerydb.backend.*;
import org.bakerydb.frontend.scenes.Inventory;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private final DBUtil dbUtil;
    private final Inventory inventory;

    public Main() {
        this.dbUtil = new DBUtil();
        System.out.println("Connected: " + dbUtil.isConnected());

        this.inventory = new Inventory(this.dbUtil);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(this.inventory.getScene());

        primaryStage.setTitle("BakeryDB");
        primaryStage.show();
    }
}
