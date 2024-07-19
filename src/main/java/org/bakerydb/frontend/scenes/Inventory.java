package org.bakerydb.frontend.scenes;

import java.util.ArrayList;

import org.bakerydb.backend.DBUtil;
import org.bakerydb.backend.items.InventoryItem;
import org.bakerydb.frontend.layouts.InventoryListing;
import org.bakerydb.util.Result;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Inventory {
    private DBUtil dbUtil;
    private VBox root;
    private Scene scene;

    public Inventory(DBUtil dbUtil) {
        this.dbUtil = dbUtil;
        this.root = new VBox();

        ObservableList<Node> children = this.root.getChildren();
        Result<ArrayList<InventoryItem>> inventoryItems = this.dbUtil.inventoryUtil.fetchAll();

        if (inventoryItems.isErr()) {
            children.add(new Label(inventoryItems.getError()));
        } else {
            inventoryItems.getValue().stream()
                .map(InventoryListing::from)
                .forEach(children::add);
        }

        this.scene = new Scene(this.root);
    }

    public Scene getScene() {
        return this.scene;
    }

    public VBox getRoot() {
        return this.root;
    }
}
