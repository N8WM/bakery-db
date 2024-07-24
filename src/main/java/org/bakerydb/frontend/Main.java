package org.bakerydb.frontend;

import java.io.IOException;

import org.bakerydb.backend.*;
import org.bakerydb.frontend.controllers.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    public Main() {
        System.out.println("Connected: " + DBManager.getInstance().isConnected());
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

    public static void showStatusMessage(
        String title,
        String message,
        boolean isError
    ) {
        FXMLLoader fxmlLoader = loader("views/StatusMessage.fxml");
        DialogPane dialogPane;

        try {
            dialogPane = fxmlLoader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        StatusMessageController statusMessageController = fxmlLoader.getController();
        statusMessageController.setMessage(message);
        statusMessageController.setError(isError);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle(title);

        dialog.showAndWait();
    }
}
