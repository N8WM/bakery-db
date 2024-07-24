package org.bakerydb.backend.models;

import java.sql.*;

import org.bakerydb.util.Model;
import org.bakerydb.util.ModelAttribute;

import javafx.util.converter.*;

public class InventoryItem extends Model<InventoryItem> {
    private ModelAttribute<Integer> invId;
    private ModelAttribute<String> name;
    private ModelAttribute<Float> quantity;
    private ModelAttribute<String> unit;
    private ModelAttribute<Float> reorderLevel;

    public InventoryItem(Integer invId, String name, Float quantity, String unit, Float reorderLevel) {
        super("Inventory",
            new ModelAttribute<Integer>(invId, "invId", Integer.class)
                .setDisplayName("ID")
                .setConverter(IntegerStringConverter.class)
                .setUserEditable(false)
                .setKey(true),
            new ModelAttribute<String>(name, "name", String.class)
                .setDisplayName("Name"),
            new ModelAttribute<Float>(quantity, "quantity", Float.class)
                .setDisplayName("Quantity")
                .setConverter(FloatStringConverter.class),
            new ModelAttribute<String>(unit, "unit", String.class)
                .setDisplayName("Unit"),
            new ModelAttribute<Float>(reorderLevel, "reorderLevel", Float.class)
                .setDisplayName("Reorder Level")
                .setConverter(FloatStringConverter.class)
        );

        this.invId = this.getAttribute("invId");
        this.name = this.getAttribute("name");
        this.quantity = this.getAttribute("quantity");
        this.unit = this.getAttribute("unit");
        this.reorderLevel = this.getAttribute("reorderLevel");
    }

    public InventoryItem() {
        this(null, null, null, null, null);
    }

    public InventoryItem(ResultSet result) throws SQLException {
        this();
        this.updateFromSQL(result);
    }

    @Override
    public InventoryItem clone() {
        return new InventoryItem(
            this.invId.getValue(),
            this.name.getValue(),
            this.quantity.getValue(),
            this.unit.getValue(),
            this.reorderLevel.getValue()
        );
    }
}
