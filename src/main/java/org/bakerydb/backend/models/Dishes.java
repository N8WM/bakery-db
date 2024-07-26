package org.bakerydb.backend.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.bakerydb.backend.DBConnection;
import org.bakerydb.backend.DBManager;
import org.bakerydb.util.Model;
import org.bakerydb.util.ModelAttribute;

import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class Dishes extends Model<Dishes> {
    private ModelAttribute<Integer> dishId;
    private ModelAttribute<String> name;
    private ModelAttribute<Float> price;
    private ModelAttribute<String> category;
    private ModelAttribute<String> description;
    private List<String> ingredients;

    public Dishes(Integer dishId, String name, Float price, String category, String description) {
        super("Dishes",
            new ModelAttribute<Integer>(dishId, "dishId", Integer.class)
                .setDisplayName("ID")
                .setConverter(IntegerStringConverter.class)
                .setUserEditable(false)
                .setKey(true),
            new ModelAttribute<String>(name, "name", String.class)
                .setDisplayName("Name"),
            new ModelAttribute<Float>(price, "price", Float.class)
                .setDisplayName("Price")
                .setConverter(FloatStringConverter.class),
            new ModelAttribute<String>(category, "category", String.class)
                .setDisplayName("Category"),
            new ModelAttribute<String>(description, "description", String.class)
                .setDisplayName("Description")
        );

        this.dishId = this.getAttribute("dishId");
        this.name = this.getAttribute("name");
        this.price = this.getAttribute("price");
        this.category = this.getAttribute("category");
        this.description = this.getAttribute("description");
        this.ingredients = new ArrayList<>();

        try {
            this.loadIngredients();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dishes() {
        this(null, null, null, null, null);
    }

    public Dishes(ResultSet result) throws SQLException {
        this();
        this.updateFromSQL(result);
        this.loadIngredients();
    }

    @Override
    public void updateFromSQL(ResultSet result) throws SQLException {
        super.updateFromSQL(result);
        this.loadIngredients();
    }

    private void loadIngredients() throws SQLException {
        if (this.dishId.getValue() != null) {
            DBConnection conn = DBManager.getDBConnection();
            String query = "SELECT Inventory.name FROM Ingredients " +
                           "JOIN Inventory ON Ingredients.invId = Inventory.invId " +
                           "WHERE Ingredients.dishId = ?";
            PreparedStatement stmt = conn.connection.prepareStatement(query);
            stmt.setInt(1, this.dishId.getValue());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                this.ingredients.add(rs.getString("name"));
            }

            rs.close();
            stmt.close();
        }
    }

    public Integer getDishId() {
        return dishId.getValue();
    }

    public String getName() {
        return name.getValue();
    }

    public Float getPrice() {
        return price.getValue();
    }

    public String getCategory() {
        return category.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    @Override
    public Dishes clone() {
        return new Dishes(
            this.dishId.getValue(),
            this.name.getValue(),
            this.price.getValue(),
            this.category.getValue(),
            this.description.getValue()
        );
    }
}
