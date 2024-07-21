package org.bakerydb.backend.models;

import java.sql.*;
import java.util.ArrayList;

import javafx.beans.property.*;

public class InventoryItem {
    private IntegerProperty invId;
    private StringProperty name;
    private FloatProperty quantity;
    private StringProperty unit;
    private FloatProperty reorderLevel;

    public InventoryItem(Integer invId, String name, Float quantity, String unit, Float reorderLevel) {
        this.invId = new SimpleIntegerProperty(invId);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleFloatProperty(quantity);
        this.unit = new SimpleStringProperty(unit);
        this.reorderLevel = new SimpleFloatProperty(reorderLevel);
    }

    public InventoryItem(ResultSet result) throws SQLException {
        this(
            result.getInt("invId"),
            result.getString("name"),
            result.getFloat("quantity"),
            result.getString("unit"),
            result.getFloat("reorderLevel")
        );
    }

    public InventoryItem() {
        this.invId = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.quantity = new SimpleFloatProperty();
        this.unit = new SimpleStringProperty();
        this.reorderLevel = new SimpleFloatProperty();
    }

    public static ArrayList<InventoryItem> list(ResultSet result) throws SQLException {
        ArrayList<InventoryItem> inventoryList = new ArrayList<InventoryItem>();
        while (result.next()) {
            inventoryList.add(new InventoryItem(result));
        }
        return inventoryList;
    }

    public InventoryItem clone() {
        return new InventoryItem(
            this.invId.get(),
            this.name.get(),
            this.quantity.get(),
            this.unit.get(),
            this.reorderLevel.get()
        );
    }

    public void update(InventoryItem other) {
        this.invId.set(other.getInvId());
        this.name.set(other.getName());
        this.quantity.set(other.getQuantity());
        this.unit.set(other.getUnit());
        this.reorderLevel.set(other.getReorderLevel());
    }

    public Integer getInvId() {
        return this.invId.get();
    }

    public IntegerProperty invIdProperty() {
        return this.invId;
    }

    public void setInvId(Integer invId) {
        this.invId.set(invId);
    }

    public String getName() {
        return this.name.get();
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Float getQuantity() {
        return this.quantity.get();
    }

    public FloatProperty quantityProperty() {
        return this.quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity.set(quantity);
    }

    public String getUnit() {
        return this.unit.get();
    }

    public StringProperty unitProperty() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public Float getReorderLevel() {
        return this.reorderLevel.get();
    }

    public FloatProperty reorderLevelProperty() {
        return this.reorderLevel;
    }

    public void setReorderLevel(Float reorderLevel) {
        this.reorderLevel.set(reorderLevel);
    }
}
