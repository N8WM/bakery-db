package org.bakerydb.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.bakerydb.backend.DBUtil;

public abstract class Model<T extends Model<T>> {

    public abstract T clone();

    private ArrayList<ModelAttribute<?>> attributes;
    private HashMap<String, ModelAttribute<?>> attributeMap;
    private String tableName;

    public Model(String tableName, ModelAttribute<?>... attributes) {
        this.tableName = tableName;
        this.attributes = new ArrayList<ModelAttribute<?>>();
        for (ModelAttribute<?> a : attributes)
            this.attributes.add(a);
        this.attributeMap = new HashMap<String, ModelAttribute<?>>();
        for (ModelAttribute<?> a : attributes)
            this.attributeMap.put(a.getAlias(), a);
    }

    public ArrayList<ModelAttribute<?>> getAttributes() {
        return this.attributes;
    }

    public void updateFromSQL(ResultSet result) throws SQLException {
        for (ModelAttribute<?> a : this.attributes)
            if (a.isDbColumn()) a.updateFromSQL(result);
    }

    public void update(T other) {
        for (int i = 0; i < this.attributes.size(); i++)
            this.attributes.get(i).update(other.getAttributes().get(i));
    }

    public boolean validate() {
        boolean isValid = true;

        for (ModelAttribute<?> p : this.attributes)
            isValid &= p.validate();

        return isValid;
    }

    @SuppressWarnings("unchecked")
    public <U> ModelAttribute<U> getAttribute(String attrAlias) {
        return (ModelAttribute<U>) this.attributeMap.get(attrAlias);
    }

    public Result<Void> addToDB() {
        if (!DBUtil.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "INSERT INTO " + this.tableName + " (";
        query += this.attributes.stream()
            .filter(a -> !a.isKey() && a.isDbColumn())
            .map(a -> a.getAlias())
            .collect(Collectors.joining(", "));
        query += ") VALUES (";
        query += this.attributes.stream()
            .filter(a -> !a.isKey() && a.isDbColumn())
            .map(a -> "?")
            .collect(Collectors.joining(", "));
        query += ")";

        try {
            PreparedStatement stmt = DBUtil.getDBConnection().connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (ModelAttribute<?> a : attributes)
                if (!a.isKey() && a.isDbColumn())
                    stmt.setObject(i++, a.getValue());

            System.out.println(stmt.toString());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                ArrayList<String> generatedKeys = this.attributes.stream()
                    .filter(a -> a.isKey() && a.isDbColumn())
                    .map(a -> a.getAlias())
                    .collect(Collectors.toCollection(ArrayList::new));
                for (String k : generatedKeys) {
                    if (keys.next()) {
                        ModelAttribute<Integer> ktmp = this.getAttribute(k);
                        ktmp.setValue(keys.getInt(1));
                    } else {
                        return Result.err(ErrorMessage.MISSING_KEY);
                    }
                }
            } else {
                return Result.err(ErrorMessage.NO_CHANGE);
            }

            return Result.ok();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

    public Result<Void> updateDB() {
        if (!DBUtil.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "UPDATE " + this.tableName + " SET ";
        query += this.attributes.stream()
            .filter(a -> !a.isKey() && a.isDbColumn())
            .map(a -> a.getAlias() + " = ?")
            .collect(Collectors.joining(", "));
        query += " WHERE ";
        query += this.attributes.stream()
            .filter(a -> a.isKey() && a.isDbColumn())
            .map(a -> a.getAlias() + " = ?")
            .collect(Collectors.joining(" AND "));

        try {
            PreparedStatement stmt = DBUtil.getDBConnection().connection.prepareStatement(query);

            int i = 1;
            for (ModelAttribute<?> a : this.attributes)
                if (!a.isKey() && a.isDbColumn())
                    stmt.setObject(i++, a.getValue());
            for (ModelAttribute<?> a : this.attributes)
                if (a.isKey() && a.isDbColumn())
                    stmt.setObject(i++, a.getValue());

            System.out.println(stmt.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    public Result<Void> deleteFromDB() {
        if (!DBUtil.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "DELETE FROM " + this.tableName + " WHERE ";
        query += this.attributes.stream()
            .filter(a -> a.isKey() && a.isDbColumn())
            .map(a -> a.getAlias() + " = ?")
            .collect(Collectors.joining(" AND "));

        try {
            PreparedStatement stmt = DBUtil.getDBConnection().connection.prepareStatement(query);
            for (int i = 0; i < this.attributes.size(); i++)
                if (this.attributes.get(i).isKey() && this.attributes.get(i).isDbColumn())
                    stmt.setObject(i + 1, this.attributes.get(i).getValue());
            System.out.println(stmt.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
        return Result.ok();
    }

    public Result<T> fetchOneDB(HashMap<String, Object> keys) {
        if (!DBUtil.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM " + this.tableName + " WHERE ";
        query += keys.keySet().stream()
            .map(k -> k + " = ?")
            .collect(Collectors.joining(" AND "));
        query += " LIMIT 1";
        
        try {
            PreparedStatement stmt = DBUtil.getDBConnection().connection.prepareStatement(query);

            int i = 1;
            for (String key : keys.keySet())
                stmt.setObject(i++, keys.get(key));

            System.out.println(query);
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                T item = this.clone();
                item.updateFromSQL(result);
                return Result.ok(item);
            } else {
                return Result.err(ErrorMessage.NO_RESULT);
            }
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }

    public Result<ArrayList<T>> fetchAllDB() {
        if (!DBUtil.isConnected())
            return Result.err(ErrorMessage.NO_CONNECTION);

        String query = "SELECT * FROM " + this.tableName;

        try {
            PreparedStatement stmt = DBUtil.getDBConnection().connection.prepareStatement(query);
            System.out.println(query);
            ResultSet result = stmt.executeQuery();

            ArrayList<T> items = new ArrayList<T>();
            while (result.next()) {
                T item = this.clone();
                item.updateFromSQL(result);
                items.add(item);
            }

            return Result.ok(items);
        } catch (SQLException e) {
            return Result.err(e.toString());
        }
    }
}
