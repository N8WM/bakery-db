package org.bakerydb.backend.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import javafx.beans.property.*;

public class HoursItem {
    private IntegerProperty hid;
    private IntegerProperty emplId;
    private ObjectProperty<Date> clockedIn;
    private ObjectProperty<Date> clockedOut;

    public HoursItem(Integer hid, Integer emplId, Date clockedIn, Date clockedOut) {
        this.hid = new SimpleIntegerProperty(hid);
        this.emplId = new SimpleIntegerProperty(emplId);
        this.clockedIn = new SimpleObjectProperty<>(clockedIn);
        this.clockedOut = new SimpleObjectProperty<>(clockedOut);
    }

    public HoursItem(ResultSet result) throws SQLException {
        this(
                result.getInt("hid"),
                result.getInt("emplId"),
                result.getTimestamp("clockedIn"),
                result.getTimestamp("clockedOut")
        );
    }

    public HoursItem() {
        this.hid = new SimpleIntegerProperty();
        this.emplId = new SimpleIntegerProperty();
        this.clockedIn = new SimpleObjectProperty<>();
        this.clockedOut = new SimpleObjectProperty<>();
    }

    public static ArrayList<HoursItem> list(ResultSet result) throws SQLException {
        ArrayList<HoursItem> hoursList = new ArrayList<>();
        while (result.next()) {
            hoursList.add(new HoursItem(result));
        }
        return hoursList;
    }

    public HoursItem clone() {
        return new HoursItem(
                this.hid.get(),
                this.emplId.get(),
                this.clockedIn.get(),
                this.clockedOut.get()
        );
    }

    public void update(HoursItem other) {
        this.hid.set(other.getHid());
        this.emplId.set(other.getEmplId());
        this.clockedIn.set(other.getClockedIn());
        this.clockedOut.set(other.getClockedOut());
    }

    public Integer getHid() {
        return this.hid.get();
    }

    public IntegerProperty hidProperty() {
        return this.hid;
    }

    public void setHid(Integer hid) {
        this.hid.set(hid);
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

    public Date getClockedIn() {
        return this.clockedIn.get();
    }

    public ObjectProperty<Date> clockedInProperty() {
        return this.clockedIn;
    }

    public void setClockedIn(Date clockedIn) {
        this.clockedIn.set(clockedIn);
    }

    public Date getClockedOut() {
        return this.clockedOut.get();
    }

    public ObjectProperty<Date> clockedOutProperty() {
        return this.clockedOut;
    }

    public void setClockedOut(Date clockedOut) {
        this.clockedOut.set(clockedOut);
    }
}
