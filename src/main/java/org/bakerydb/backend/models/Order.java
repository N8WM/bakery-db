package org.bakerydb.backend.models;

import java.util.Date;

import org.bakerydb.util.Model;
import org.bakerydb.util.ModelAttribute;

import javafx.util.converter.*;

public class Order extends Model<Order> {
    private ModelAttribute<Integer> orderId;
    private ModelAttribute<String> ccn;
    private ModelAttribute<Date> date;
    private ModelAttribute<Integer> emplId;

    public Order(Integer orderId, String ccn, Date date, Integer emplId) {
        super("Orders",
            new ModelAttribute<Integer>(orderId, "orderId", Integer.class)
                .setDisplayName("ID")
                .setConverter(IntegerStringConverter.class)
                .setUserEditable(false)
                .setKey(true),
            new ModelAttribute<String>(ccn, "ccn", String.class)
                .setDisplayName("Credit Card Number"),
            new ModelAttribute<Date>(date, "date", Date.class)
                .setDisplayName("Date")
                .setConverter(DateStringConverter.class),
            new ModelAttribute<Integer>(emplId, "emplId", Integer.class)
                .setDisplayName("Employee ID")
        );

        this.orderId = this.getAttribute("orderId");
        this.ccn = this.getAttribute("ccn");
        this.date = this.getAttribute("date");
        this.emplId = this.getAttribute("emplId");
    }

    public Order() {
        this(null, null, null, null);
    }

    @Override
    public Order clone() {
        return new Order(
            this.orderId.getValue(),
            this.ccn.getValue(),
            this.date.getValue(),
            this.emplId.getValue()
        );
    }
}
