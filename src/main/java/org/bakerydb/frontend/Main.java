package org.bakerydb.frontend;

import org.bakerydb.backend.*;
import org.bakerydb.frontend.controllers.RootController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
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
        FXMLLoader rootLoader = loader("views/Root.fxml");
        Parent root = rootLoader.load();
        RootController rootController = rootLoader.getController();

        AnchorPane inventoryList = loader("views/Inventory.fxml").load();
        rootController.getTabs().add(new Tab("Inventory", inventoryList));
        AnchorPane inventoryList2 = loader("views/Inventory.fxml").load();
        rootController.getTabs().add(new Tab("Inventory2", inventoryList2));

        Scene sc = new Scene(root, 1080, 600);

        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(450);
        primaryStage.setTitle("BakeryDB");
        primaryStage.setScene(sc);

        sc.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            Node focusedNode = sc.getFocusOwner();
            if (focusedNode != null) {
                if (!focusedNode.getBoundsInParent().contains(
                    event.getX(),
                    event.getY()
                )) {
                    root.requestFocus();
                }
            }
        });

        primaryStage.show();
    }

    public static FXMLLoader loader(String name) {
        String resource = name;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(
            Thread.currentThread()
            .getContextClassLoader()
            .getResource(resource)
        );

        return fxmlLoader;
    }
}
