package org.bakerydb.frontend.layouts;

import org.bakerydb.backend.items.*;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class InventoryListing {

    public static HBox from(InventoryItem item) {
        HBox root = new HBox();
        ObservableList<Node> children = root.getChildren();

        children.add(new Label(item.name));
        children.add(new Label(item.quantity.toString()));
        children.add(new Label(item.unit));
        children.add(new Label(item.reorderLevel.toString()));

        return root;
    }
}
