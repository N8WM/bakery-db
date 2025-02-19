package org.bakerydb.backend.models;

import java.util.function.BiPredicate;

import org.bakerydb.util.Model;
import org.bakerydb.util.ModelAttribute;

import javafx.util.converter.*;

public class InventoryItem extends Model<InventoryItem> {
    private ModelAttribute<Integer> invId;
    private ModelAttribute<String> name;
    private ModelAttribute<Float> quantity;
    private ModelAttribute<String> unit;
    private ModelAttribute<Float> reorderLevel;

    // Predicate to calculate a cosmetic attribute called "low" (not in the database)
    private static BiPredicate<Float, Float> lowPredicate = (q, rl) -> q != null && rl != null && q < rl;

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
                .setConverter(FloatStringConverter.class),

            // non-database/cosmetic attribute "low"
            new ModelAttribute<Boolean>(lowPredicate.test(quantity, reorderLevel), "low", Boolean.class)
                .setDisplayName("Low")
                .setConverter(BooleanStringConverter.class)
                .setUserEditable(false)
                .setDbColumn(false)
        );

        this.invId = this.getAttribute("invId");
        this.name = this.getAttribute("name");
        this.quantity = this.getAttribute("quantity");
        this.unit = this.getAttribute("unit");
        this.reorderLevel = this.getAttribute("reorderLevel");

        this.quantity.getProperty().addListener((obs, oldVal, newVal) ->
            this.getAttribute("low").setValue(lowPredicate.test(newVal, this.reorderLevel.getValue())));
        this.reorderLevel.getProperty().addListener((obs, oldVal, newVal) ->
            this.getAttribute("low").setValue(lowPredicate.test(this.quantity.getValue(), newVal)));
    }

    public InventoryItem() {
        this(null, null, null, null, null);
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
