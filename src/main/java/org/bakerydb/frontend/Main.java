package org.bakerydb.frontend;

import org.bakerydb.backend.*;
import org.bakerydb.frontend.controllers.*;

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
        System.out.println("Connected: " + DBManager.isConnected());
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader rootLoader = FEUtil.loader("views/Root.fxml");
        Parent root = rootLoader.load();
        RootController rootController = rootLoader.getController();

        AnchorPane inventoryList = FEUtil.loader("views/Inventory.fxml").load();
        rootController.getTabs().add(new Tab("Inventory", inventoryList));
        AnchorPane orders = FEUtil.loader("views/Orders.fxml").load();
        rootController.getTabs().add(new Tab("Orders", orders));
        AnchorPane dishes = FEUtil.loader("views/Dishes.fxml").load();
        rootController.getTabs().add(new Tab("Dishes", dishes));
        AnchorPane employeeList = FEUtil.loader("views/Employee.fxml").load();
        rootController.getTabs().add(new Tab("Employees", employeeList));
        AnchorPane timesheet = FEUtil.loader("views/Hours.fxml").load();
        rootController.getTabs().add(new Tab("Timesheet", timesheet));

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
}
