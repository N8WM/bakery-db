package org.bakerydb.frontend;

import org.bakerydb.backend.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public Main() {
        System.out.println("Connected: " + DBUtil.isConnected());
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args.length == 1 && args[0].equals("--create-tables")) {
                DBSetup.run();
                System.exit(0);
            }
            System.out.println("The only valid argument is `--create-tables`");
            System.exit(1);
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(
            Thread.currentThread()
            .getContextClassLoader()
            .getResource("views/Inventory.fxml")
        );
        Scene sc = new Scene(root, 1080, 600);
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(450);

        primaryStage.setTitle("BakeryDB");
        primaryStage.setScene(sc);

        primaryStage.show();
    }
}
