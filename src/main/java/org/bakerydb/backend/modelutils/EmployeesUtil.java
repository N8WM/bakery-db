package org.bakerydb.backend.modelutils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bakerydb.backend.DBConnection;
import org.bakerydb.backend.models.EmployeesItems;
import org.bakerydb.util.ErrorMessage;
import org.bakerydb.util.Result;
import org.bakerydb.backend.DBManager;

public final class EmployeesUtil {

    private EmployeesUtil() {
        throw new UnsupportedOperationException("Util class");
    }

    private static PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        DBConnection DB = DBManager.getInstance().getDBConnection();
        PreparedStatement stmt = DB.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        return stmt;
    }

    /**
     * Add a new employee to Employees
     * @param item the EmployeesItems to add
     * @return a Result containing either the resulting key or an error message
     */
    public static Result<Integer> newEmployee(EmployeesItems item) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "INSERT INTO Employees (firstName, middleInitial, lastName, role, dateHired) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = prepareStatement(query, item.getFirstName(), item.getmiddleInitial(), item.getLastName(), item.getRole(), item.getDateHired());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next())
                    return Result.ok(keys.getInt(1));
                else
                    return Result.err(ErrorMessage.NO_KEY);
            } else {
                return Result.err(ErrorMessage.NO_CHANGE);
            }
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

    /**
     * Remove an employee from Employees
     * @param emplId the id of the employee
     * @return a Result containing either a Void or an error message
     */
    public static Result<Void> deleteEmployee(Integer emplId) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "DELETE FROM Employees WHERE emplId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, emplId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    /**
     * Update an employee in Employees
     * @param employee the EmployeesItems to update
     * @return a Result containing either a Void or an error message
     */
    public static Result<Void> update(EmployeesItems employee) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "UPDATE Employees SET firstName = ?, middleInitial = ?, lastName = ?, role = ?, dateHired = ? WHERE emplId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, employee.getFirstName(), employee.getmiddleInitial(), employee.getLastName(), employee.getRole(), employee.getDateHired(), employee.getemplId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    /**
     * Fetch all employees from Employees
     * @return a Result containing either an ArrayList of EmployeesItems or an error message
     */
    public static Result<ArrayList<EmployeesItems>> fetchAll() {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM Employees";
        try {
            PreparedStatement stmt = prepareStatement(query);
            return Result.ok(EmployeesItems.list(stmt.executeQuery()));
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

    /**
     * Fetch a single employee from Employees
     * @param emplId the id of the employee
     * @return a Result containing either an EmployeesItems item or an error message
     */
    public static Result<EmployeesItems> fetch(Integer emplId) {
        if (!DBManager.getInstance().isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM Employees WHERE emplId = ?";
        try {
            PreparedStatement stmt = prepareStatement(query, emplId);
            ResultSet result = stmt.executeQuery();
            if (result.next())
                return Result.ok(new EmployeesItems(result));
            else
                return Result.err(ErrorMessage.ITEM_NOT_FOUND);
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }
}
