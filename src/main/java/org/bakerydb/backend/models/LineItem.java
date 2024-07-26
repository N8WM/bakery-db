package org.bakerydb.backend.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.function.BiFunction;

import org.bakerydb.backend.*;
import org.bakerydb.util.*;

import javafx.scene.control.TextArea;
import javafx.util.converter.*;

public class LineItem extends Model<LineItem> {
    private ModelAttribute<Integer> dishId;
    private ModelAttribute<Integer> orderId;
    private ModelAttribute<String> dishName;
    private ModelAttribute<Integer> quantity;
    private ModelAttribute<Float> dishPrice;
    private ModelAttribute<Float> price;
    private ModelAttribute<String> specialInstructions;

    private static BiFunction<Integer, Float, Float> calcTotalPrice = (quantity, dishPrice) -> {
        return (quantity == null || dishPrice == null) ? null : quantity * dishPrice;
    };

    private static Order orderViewing;

    public LineItem(
        Integer dishId,
        Integer orderId,
        String dishName,
        Integer quantity,
        Float dishPrice,
        String specialInstructions
    ) {
        super("LineItems",
            new ModelAttribute<Integer>(dishId, "dishId", Integer.class)
                .setConverter(IntegerStringConverter.class)
                .setUserEditable(false)
                .setUserVisible(false)
                .setKey(true),
            new ModelAttribute<Integer>(orderId, "orderId", Integer.class)
                .setConverter(IntegerStringConverter.class)
                .setUserEditable(false)
                .setUserVisible(false)
                .setKey(true),
            new ModelAttribute<String>(dishName, "dishName", String.class)
                .setDisplayName("Dish Name")
                .setDbColumn(false),
            new ModelAttribute<Integer>(quantity, "quantity", Integer.class)
                .setDisplayName("Quantity")
                .setConverter(IntegerStringConverter.class),
            new ModelAttribute<Float>(dishPrice, "dishPrice", Float.class)
                .setDisplayName("Price ea.")
                .setUserEditable(false)
                .setDbColumn(false)
                .setConverter(FloatStringConverter.class),
            new ModelAttribute<Float>(calcTotalPrice.apply(quantity, dishPrice), "price", Float.class)
                .setDisplayName("Total")
                .setUserEditable(false)
                .setDbColumn(false)
                .setConverter(FloatStringConverter.class),
            new ModelAttribute<String>(specialInstructions, "specialInstructions", String.class)
                .setDisplayName("Special Instructions")
                .setField(TextArea.class)
        );

        this.dishId = this.getAttribute("dishId");
        this.orderId = this.getAttribute("orderId");
        this.dishName = this.getAttribute("dishName");
        this.quantity = this.getAttribute("quantity");
        this.dishPrice = this.getAttribute("dishPrice");
        this.price = this.getAttribute("price");
        this.specialInstructions = this.getAttribute("specialInstructions");

        this.quantity.getProperty().addListener((observable, oldValue, newValue) ->
            this.price.setValue(calcTotalPrice.apply(newValue.intValue(), this.dishPrice.getValue())));
        this.dishPrice.getProperty().addListener((observable, oldValue, newValue) ->
            this.price.setValue(calcTotalPrice.apply(this.quantity.getValue(), newValue.floatValue())));
    }

    public LineItem() {
        this(null, null, null, null, null, null);
    }

    @Override
    public LineItem clone() {
        return new LineItem(
            this.dishId.getValue(),
            this.orderId.getValue(),
            this.dishName.getValue(),
            this.quantity.getValue(),
            this.dishPrice.getValue(),
            this.specialInstructions.getValue()
        );
    }

    public static void setOrderViewing(Order order) {
        LineItem.orderViewing = order;
    }

    public static Order getOrderViewing(Order order) {
        return LineItem.orderViewing;
    }

    private static Result<Void> autoCommit(Boolean enabled) {
        try {
            DBManager.getDBConnection().connection.setAutoCommit(enabled);
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        return Result.ok();
    }

    private Result<Void> fillInMissingData(ArrayList<PreparedStatement> toClose) {
        Result<Void> result = closeAll(toClose);
        if (result.isErr())
            return result;

        String q = """
        SELECT dishId, price FROM Dishes WHERE name = ? LIMIT 1
        """;

        try {
            PreparedStatement stmt = DBManager.getDBConnection().connection.prepareStatement(q);
            stmt.setString(1, this.dishName.getValue());
            toClose.add(stmt);

            System.out.println(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next())
                return Result.err("Internal - No LineItem found");

            this.dishId.setValue(rs.getInt("dishId"));
            this.orderId.setValue((Integer) orderViewing.getAttribute("orderId").getValue());
            this.dishPrice.setValue(rs.getFloat("price"));
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        return Result.ok();
    }

    private Result<Void> createInvQuantView(ArrayList<PreparedStatement> toClose) {
        Result<Void> result = closeAll(toClose);
        if (result.isErr())
            return result;

        String q1 = """
        DROP VIEW IF EXISTS InvQuant
        """;
        String q2 = """
        CREATE VIEW InvQuant as (
            SELECT Inv.invId, Ing.quantity * ? as required
            FROM Ingredients Ing JOIN Inventory Inv ON Ing.invId = Inv.invId
            WHERE Ing.dishId = ?
        )
        """;

        try {
            PreparedStatement stmt = DBManager.getDBConnection().connection.prepareStatement(q1);
            toClose.add(stmt);

            System.out.println(stmt.toString());
            stmt.execute();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        try {
            PreparedStatement stmt = DBManager.getDBConnection().connection.prepareStatement(q2);
            stmt.setInt(1, this.quantity.getValue());
            stmt.setInt(2, this.dishId.getValue());
            toClose.add(stmt);

            System.out.println(stmt.toString());
            stmt.execute();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        return Result.ok();
    }

    private Result<Void> dropInvQuantView(ArrayList<PreparedStatement> toClose) {
        Result<Void> result = closeAll(toClose);
        if (result.isErr())
            return result;

        String q = """
        DROP VIEW IF EXISTS InvQuant
        """;

        try {
            PreparedStatement stmt = DBManager.getDBConnection().connection.prepareStatement(q);
            toClose.add(stmt);

            System.out.println(stmt.toString());
            stmt.execute();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        return Result.ok();
    }

    /** Call on clone of previous values **/
    private Result<Void> replaceInventory(ArrayList<PreparedStatement> toClose) {
        Result<Void> result = closeAll(toClose);
        if (result.isErr())
            return result;

        result = this.createInvQuantView(toClose);
        if (result.isErr())
            return result;

        String q = """
        UPDATE Inventory Inv NATURAL JOIN InvQuant IQ
        SET Inv.quantity = Inv.quantity + IQ.required
        """;

        try {
            PreparedStatement stmt = DBManager.getDBConnection().connection.prepareStatement(q);
            toClose.add(stmt);

            System.out.println(stmt.toString());
            if (stmt.executeUpdate() == 0)
                return Result.err("Internal - Nothing was replaced in Inventory");
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        return dropInvQuantView(toClose);
    }

    private Result<Void> removeFromInventory(ArrayList<PreparedStatement> toClose) {
        Result<Void> result = closeAll(toClose);
        if (result.isErr())
            return result;

        result = this.createInvQuantView(toClose);
        if (result.isErr())
            return result;

        String q = """
        UPDATE Inventory Inv NATURAL JOIN InvQuant IQ
        SET Inv.quantity = Inv.quantity - IQ.required
        """;

        try {
            PreparedStatement stmt = DBManager.getDBConnection().connection.prepareStatement(q);
            toClose.add(stmt);

            System.out.println(stmt.toString());
            if (stmt.executeUpdate() == 0)
                return Result.err("Internal - Nothing was removed from Inventory");
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        return dropInvQuantView(toClose);
    }

    private Result<Void> insertLineItem(ArrayList<PreparedStatement> toClose) {
        Result<Void> result = closeAll(toClose);
        if (result.isErr())
            return result;

        String q = """
        INSERT INTO LineItems (dishId, orderId, quantity, specialInstructions) VALUES
        (?, ?, ?, ?)
        """;

        try {
            PreparedStatement stmt = DBManager.getDBConnection().connection.prepareStatement(q);
            stmt.setInt(1, this.dishId.getValue());
            stmt.setInt(2, this.orderId.getValue());
            stmt.setInt(3, this.quantity.getValue());
            stmt.setString(4, this.specialInstructions.getValue());
            toClose.add(stmt);

            System.out.println(stmt.toString());
            if (stmt.executeUpdate() == 0)
                return Result.err("Internal - Nothing was inserted into LineItems");
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        return Result.ok();
    }

    private Result<Void> deleteLineItem(ArrayList<PreparedStatement> toClose) {
        Result<Void> result = closeAll(toClose);
        if (result.isErr())
            return result;

        String q = """
        DELETE FROM LineItems WHERE dishId = ? AND orderId = ?
        """;

        try {
            PreparedStatement stmt = DBManager.getDBConnection().connection.prepareStatement(q);
            stmt.setInt(1, this.dishId.getValue());
            stmt.setInt(2, this.orderId.getValue());
            toClose.add(stmt);

            System.out.println(stmt.toString());
            if (stmt.executeUpdate() == 0)
                return Result.err("Internal - Nothing was deleted from LineItems");
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        return Result.ok();
    }

    private static Result<Void> commit() {
        try {
            DBManager.getDBConnection().connection.commit();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        return Result.ok();
    }

    private static Result<Void> closeAll(ArrayList<PreparedStatement> toClose) {
        while (!toClose.isEmpty()) {
            try {
                toClose.remove(0).close();
            } catch (SQLException e) {
                return Result.err(e.toString());
            }
        }

        return Result.ok();
    }

    private static Result<Void> rollback(ArrayList<PreparedStatement> toClose) {
        try {
            DBManager.getDBConnection().connection.setAutoCommit(true);
            DBManager.getDBConnection().connection.rollback();
            return closeAll(toClose);
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

    @Override
    public Result<Void> addToDB() {
        if (!DBManager.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        Result<Void> result;
        result = autoCommit(false);
        if (result.isErr())
            return result;

        ArrayList<PreparedStatement> toClose = new ArrayList<>();

        result = this.fillInMissingData(toClose);
        if (result.isErr()) {
            rollback(toClose);
            return result;
        }

        result = this.removeFromInventory(toClose);
        if (result.isErr()) {
            rollback(toClose);
            return result;
        }

        result = this.insertLineItem(toClose);
        if (result.isErr()) {
            rollback(toClose);
            return result;
        }

        result = commit();
        if (result.isErr()) {
            rollback(toClose);
            return result;
        }

        result = autoCommit(true);
        if (result.isErr())
            return result;

        return closeAll(toClose);
    }

    @Override
    public Result<Void> deleteFromDB() {
        if (!DBManager.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        Result<Void> result;
        result = autoCommit(false);
        if (result.isErr())
            return result;

        ArrayList<PreparedStatement> toClose = new ArrayList<>();

        result = this.replaceInventory(toClose);
        if (result.isErr()) {
            rollback(toClose);
            return result;
        }

        result = this.deleteLineItem(toClose);
        if (result.isErr()) {
            rollback(toClose);
            return result;
        }

        result = commit();
        if (result.isErr()) {
            rollback(toClose);
            return result;
        }

        result = autoCommit(true);
        if (result.isErr())
            return result;

        return closeAll(toClose);
    }

    @Override
    public Result<ArrayList<LineItem>> fetchAllDB() {
        if (!DBManager.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String q = """
        SELECT L.dishId, L.orderId, D.name, L.quantity, D.price, L.specialInstructions
        FROM LineItems L JOIN Dishes D ON L.dishId = D.dishId
        WHERE L.orderId = ?
        """;

        ArrayList<LineItem> lineItems = new ArrayList<>();
        try {
            PreparedStatement stmt = DBManager.getDBConnection().connection.prepareStatement(q);
            stmt.setInt(1, (Integer) orderViewing.getAttribute("orderId").getValue());

            System.out.println(stmt.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lineItems.add(new LineItem(
                    rs.getInt("dishId"),
                    rs.getInt("orderId"),
                    rs.getString("name"),
                    rs.getInt("quantity"),
                    rs.getFloat("price"),
                    rs.getString("specialInstructions")
                ));
            }
        } catch (SQLException e) {
            return Result.err(e.toString());
        }

        return Result.ok(lineItems);
    }
}
