package models_old;

import java.sql.*;
import java.util.ArrayList;

import javafx.beans.property.*;

public class DishesItems_old {
    private IntegerProperty dishId;
    private StringProperty name;
    private FloatProperty price;
    private StringProperty category;
    private StringProperty description;


    public DishesItems_old(Integer dishId, String name, Float price, String category, String description) {
        this.dishId = new SimpleIntegerProperty(dishId);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleFloatProperty(price);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
    }
    
    public DishesItems_old(ResultSet result) throws SQLException {
        this(
            result.getInt("dishId"),
            result.getString("name"),
            result.getFloat("price"),
            result.getString("category"),
            result.getString("description")
        );
    }

    public DishesItems_old() {
        this.dishId = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.price = new SimpleFloatProperty();
        this.category = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
    }

    public static ArrayList<DishesItems_old> list(ResultSet result) throws SQLException {
        ArrayList<DishesItems_old> dishesList = new ArrayList<DishesItems_old>();
        while (result.next()) {
            dishesList.add(new DishesItems_old(result));
        }
        return dishesList;
    }

    public DishesItems_old clone() {
        return new DishesItems_old(
            this.dishId.get(),
            this.name.get(),
            this.price.get(),
            this.category.get(),
            this.description.get()
        );
    }

    public void update(DishesItems_old other) {
        this.dishId.set(other.getDishId());
        this.name.set(other.getName());
        this.price.set(other.getprice());
        this.category.set(other.getcategory());
        this.description.set(other.getdescription());
    }

    public Integer getDishId() {
        return this.dishId.get();
    }

    public IntegerProperty dishIdProperty() {
        return this.dishId;
    }

    public void setInvId(Integer dishId) {
        this.dishId.set(dishId);
    }
////////////////////////////////////////////////////
    public String getName() {
        return this.name.get();
    }

    public StringProperty nameProperty() {
        return this.name;
    }

    public void setName(String name) {
        this.name.set(name);
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
///////////////////////////////////////////////////
    public String getcategory() {
        return this.category.get();
    }

    public StringProperty categoryProperty() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }
//////////////////////////////////////////////////
    public String getdescription() {
        return this.description.get();
    }

    public StringProperty descriptionProperty() {
        return this.description;
    }

    public void setdescription(String description) {
        this.description.set(description);
    }
    
}
