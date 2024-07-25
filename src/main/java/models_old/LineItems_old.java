package models_old;

import java.sql.*;
import java.util.ArrayList;

import javafx.beans.property.*;

public class LineItems_old {
    private IntegerProperty dishId;
    private IntegerProperty orderId;
    private IntegerProperty quantity;
    private FloatProperty price;
    private StringProperty specialInstructions;
    
    public LineItems_old(
        Integer dishId,
        Integer orderId,
        Integer quantity,
        Float price,
        String specialInstructions
        ) 
        {
        this.dishId = new SimpleIntegerProperty(dishId);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleFloatProperty(price);
        this.specialInstructions = new SimpleStringProperty(specialInstructions);
    }

    public LineItems_old(ResultSet result) throws SQLException {
        this(
            result.getInt("dishId"),
            result.getInt("orderId"),
            result.getInt("quantity"),
            result.getFloat("price"),
            result.getString("specialInstructions")
        );
    }

    public LineItems_old() {
        this.dishId = new SimpleIntegerProperty();
        this.orderId = new SimpleIntegerProperty();
        this.quantity = new SimpleIntegerProperty();
        this.price = new SimpleFloatProperty();
        this.specialInstructions = new SimpleStringProperty();
    }

    public static ArrayList<LineItems_old> list(ResultSet result) throws SQLException {
        ArrayList<LineItems_old> lineList = new ArrayList<LineItems_old>();
        while (result.next()) {
            lineList.add(new LineItems_old(result));
        }
        return lineList;
    }

    public LineItems_old clone() {
        return new LineItems_old(
            this.dishId.get(),
            this.orderId.get(),
            this.quantity.get(),
            this.price.get(),
            this.specialInstructions.get()
        );
    }

    public void update(LineItems_old other) {
        this.dishId.set(other.getdishId());
        this.orderId.set(other.getorderId());
        this.quantity.set(other.getQuantity());
        this.price.set(other.getprice());
        this.specialInstructions.set(other.getspecialInstructions());
    }

    public Integer getdishId() {
        return this.dishId.get();
    }

    public IntegerProperty dishIdProperty() {
        return this.dishId;
    }

    public void setdishId(Integer dishId) {
        this.dishId.set(dishId);
    }
////////////////////////////////////////////////////
    public Integer getorderId() {
        return this.orderId.get();
    }

    public IntegerProperty orderIdProperty() {
        return this.orderId;
    }

    public void orderIdName(Integer orderId) {
        this.orderId.set(orderId);
    }

////////////////////////////////////////////////////
    public Integer getQuantity() {
        return this.quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity.set(quantity);
    }
////////////////////////////////////////////////////
    public Float getprice() {
        return this.price.get();
    }

    public FloatProperty priceProperty() {
        return this.price;
    }

    public void setprice(Float price) {
        this.price.set(price);
    }

////////////////////////////////////////////////////
    public String getspecialInstructions() {
        return this.specialInstructions.get();
    }

    public StringProperty specialInstructionsProperty() {
        return this.specialInstructions;
    }

    public void setrole(String specialInstructions) {
        this.specialInstructions.set(specialInstructions);
    }
}
