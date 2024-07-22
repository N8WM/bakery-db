package org.bakerydb.backend.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import javafx.beans.property.*;

public class OrderItem {
    private IntegerProperty orderId;
    private StringProperty ccn;
    private ObjectProperty<Date> date;
    private IntegerProperty emplId;
    private FloatProperty total;

    public OrderItem(Integer orderId, String ccn, Date date, Integer emplId, Float total) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.ccn = new SimpleStringProperty(ccn);
        this.date = new SimpleObjectProperty<>(date);
        this.emplId = new SimpleIntegerProperty(emplId);
        this.total = new SimpleFloatProperty(total);
    }

    public OrderItem(ResultSet result) throws SQLException {
        this(
                result.getInt("orderId"),
                result.getString("ccn"),
                result.getDate("date"),
                result.getInt("emplId"),
                result.getFloat("total")
        );
    }

    public OrderItem() {
        this.orderId = new SimpleIntegerProperty();
        this.ccn = new SimpleStringProperty();
        this.date = new SimpleObjectProperty<>();
        this.emplId = new SimpleIntegerProperty();
        this.total = new SimpleFloatProperty();
    }

    public static ArrayList<OrderItem> list(ResultSet result) throws SQLException {
        ArrayList<OrderItem> orderList = new ArrayList<>();
        while (result.next()) {
            orderList.add(new OrderItem(result));
        }
        return orderList;
    }

    public OrderItem clone() {
        return new OrderItem(
                this.orderId.get(),
                this.ccn.get(),
                this.date.get(),
                this.emplId.get(),
                this.total.get()
        );
    }

    public void update(OrderItem other) {
        this.orderId.set(other.getOrderId());
        this.ccn.set(other.getCcn());
        this.date.set(other.getDate());
        this.emplId.set(other.getEmplId());
        this.total.set(other.getTotal());
    }

    public Integer getOrderId() {
        return this.orderId.get();
    }

    public IntegerProperty orderIdProperty() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId.set(orderId);
    }

    public String getCcn() {
        return this.ccn.get();
    }

    public StringProperty ccnProperty() {
        return this.ccn;
    }

    public void setCcn(String ccn) {
        this.ccn.set(ccn);
    }

    public Date getDate() {
        return this.date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public Integer getEmplId() {
        return this.emplId.get();
    }

    public IntegerProperty emplIdProperty() {
        return this.emplId;
    }

    public void setEmplId(Integer emplId) {
        this.emplId.set(emplId);
    }

    public Float getTotal() {
        return this.total.get();
    }

    public FloatProperty totalProperty() {
        return this.total;
    }

    public void setTotal(Float total) {
        this.total.set(total);
    }
}
