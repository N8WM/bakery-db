package org.bakerydb.backend.models;

import java.sql.*;
import org.bakerydb.backend.DBConnection;
import org.bakerydb.backend.DBUtil;
import org.bakerydb.util.Model;
import org.bakerydb.util.ModelAttribute;
import javafx.util.converter.*;

public class Hours extends Model<Hours> {
    private ModelAttribute<Integer> hid;
    private ModelAttribute<Integer> emplId;
    private ModelAttribute<Timestamp> clockedIn;
    private ModelAttribute<Timestamp> clockedOut;

    private String firstName;
    private String lastName;

    public Hours(Integer hid, Integer emplId, Timestamp clockedIn, Timestamp clockedOut) {
        super("Hours",
            new ModelAttribute<Integer>(hid, "hid", Integer.class)
                .setDisplayName("ID")
                .setConverter(IntegerStringConverter.class)
                .setUserEditable(false)
                .setKey(true),
            new ModelAttribute<Integer>(emplId, "emplId", Integer.class)
                .setDisplayName("Employee ID")
                .setConverter(IntegerStringConverter.class),
            new ModelAttribute<Timestamp>(clockedIn, "clockedIn", Timestamp.class)
                .setDisplayName("Clocked In"),
            new ModelAttribute<Timestamp>(clockedOut, "clockedOut", Timestamp.class)
                .setDisplayName("Clocked Out")
        );

        this.hid = this.getAttribute("hid");
        this.emplId = this.getAttribute("emplId");
        this.clockedIn = this.getAttribute("clockedIn");
        this.clockedOut = this.getAttribute("clockedOut");

        try {
            this.loadEmployeeDetails();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Hours() {
        this(null, null, null, null);
    }

    public Hours(ResultSet result) throws SQLException {
        this();
        this.updateFromSQL(result);
        this.loadEmployeeDetails();
    }

    @Override
    public void updateFromSQL(ResultSet result) throws SQLException {
        super.updateFromSQL(result);
        this.emplId.setValue(result.getInt("emplId"));
        this.loadEmployeeDetails();
    }

    private void loadEmployeeDetails() throws SQLException {
        if (this.emplId.getValue() != null) {
            DBConnection conn = DBUtil.getDBConnection();
            String query = "SELECT firstName, lastName FROM Employees WHERE emplId = ?";
            PreparedStatement stmt = conn.connection.prepareStatement(query);
            stmt.setInt(1, this.emplId.getValue());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.firstName = rs.getString("firstName");
                this.lastName = rs.getString("lastName");
            }

            rs.close();
            stmt.close();
        }
    }

    public Integer getHid() {
        return hid.getValue();
    }

    public Integer getEmplId() {
        return emplId.getValue();
    }

    public Timestamp getClockedIn() {
        return clockedIn.getValue();
    }

    public Timestamp getClockedOut() {
        return clockedOut.getValue();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public Hours clone() {
        return new Hours(
            this.hid.getValue(),
            this.emplId.getValue(),
            this.clockedIn.getValue(),
            this.clockedOut.getValue()
        );
    }
    public void setEmplId(int emplId) {
        this.emplId.setValue(emplId);
    }

    public void setClockedOut(Timestamp clockedOut) {
        this.clockedOut.setValue(clockedOut);
    }
}
