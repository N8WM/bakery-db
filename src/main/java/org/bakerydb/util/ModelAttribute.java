package org.bakerydb.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

public class ModelAttribute<T> {
    private ObjectProperty<T> property;
    private String alias;
    private String displayName;
    private TextInputControl field;
    private Label label;
    private Boolean userEditable;
    private Boolean dbColumn;
    private Boolean key;
    private NStringConverter<T> converter;
    private StringProperty textProperty;
    private Class<T> type;

    /**
     * ModelAttribute constructor
     * @param value - an initial value
     * @param alias - the name of the variable storing the attribute
     * @param type - the class of the value (i.e., String.class, Integer.class, etc.)
     *
     * @implNote Once a ModelAttribute is instantiated, there are several methods that can be used to customize the attribute
     * @implNote {@code ModelAttribute.setDisplayName(String displayName)}
     * default is value of alias
     * @implNote {@code ModelAttribute.setField(Class<? extends TextInputControl> fieldClass)}
     * default is TextField.class
     * @implNote {@code ModelAttribute.setUserEditable(Boolean userEditable)}
     * default is true
     * @implNote {@code ModelAttribute.setDbColumn(Boolean dbColumn)}
     * default is true
     * @implNote {@code ModelAttribute.setKey(Boolean key)}
     * default is false
     * @implNote {@code ModelAttribute.setConverter(<? extends StringConverter<T>> converterClass)}
     * default assumes T is a String
     */
    @SuppressWarnings("unchecked")
    public ModelAttribute(T value, String alias, Class<T> type) {
        this.property = new SimpleObjectProperty<T>(value);
        this.alias = alias;
        this.displayName = alias;
        this.userEditable = true;
        this.dbColumn = true;
        this.key = false;
        this.converter = new NStringConverter<T>(
            new StringConverter<T>() {
                @Override
                public String toString(T object) {
                    return object == null ? "" : object.toString();
                }
                @Override
                public T fromString(String string) {
                    return (T) string;
                }
            }
        );

        this.textProperty = new SimpleStringProperty();
        this.textProperty.bindBidirectional(this.property, this.converter);
        this.setLabel();
        this.setField(TextField.class);
        this.type = type;
    }

    public ModelAttribute(String alias, Class<T> type) {
        this(null, alias, type);
    }

    public String getAlias() {
        return this.alias;
    }

    public ObjectProperty<T> getProperty() {
        return this.property;
    }

    public StringConverter<T> getStringConverter() {
        return this.converter;
    }

    @SuppressWarnings("unchecked")
    public <U> ObjectProperty<U> getUncheckedProperty() {
        return (ObjectProperty<U>) this.property;
    }

    public Boolean isUserEditable() {
        return this.userEditable;
    }

    public Boolean isDbColumn() {
        return this.dbColumn;
    }

    public Boolean isKey() {
        return this.key;
    }

    public StringProperty textProperty() {
        return this.textProperty;
    }

    public Class<T> getType() {
        return this.type;
    }

    public T getValue() {
        return this.property.getValue();
    }

    public ModelAttribute<T> setValue(T value) {
        this.property.setValue(value);
        return this;
    }

    /** Unsafe cast - only use when types are compatible */
    @SuppressWarnings("unchecked")
    public void update(ModelAttribute<?> other) {
        this.setValue((T) other.getValue());
    }

    public void updateFromSQL(ResultSet result) throws SQLException {
        this.property.setValue(result.getObject(this.alias, this.type));
    }

    public ModelAttribute<T> setDisplayName(String displayName) {
        this.displayName = displayName;
        this.label.setText(displayName);
        return this;
    }

    private void setLabel() {
        this.label = new Label(this.displayName);
        this.label.setMnemonicParsing(true);
        this.label.setAlignment(Pos.CENTER_RIGHT);
        this.label.setTextAlignment(TextAlignment.RIGHT);
        this.label.setPadding(new Insets(0, 0, 0, 10));
        this.label.setWrapText(true);
        this.label.setPrefWidth(100);
        this.label.setMaxWidth(100);
    }

    public ModelAttribute<T> setField(Class<? extends TextInputControl> fieldClass) {
        if (this.field != null)
            this.field.textProperty().unbindBidirectional(this.textProperty);

        try {
            this.field = fieldClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(
                "Field \""
                + fieldClass.getSimpleName()
                + "\" could not be instantiated"
            );
        }
                
        this.field.setPromptText(this.alias);
        this.field.textProperty().bindBidirectional(this.textProperty);
        this.field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty())
                this.field.setStyle("-fx-border-color: red;");
            else if (this.converter.isValidStr(newValue))
                this.field.setStyle(null);
            else
                this.field.setStyle("-fx-border-color: red;");
        });

        this.label.setLabelFor(this.field);
        return this;
    }

    public void addToGrid(GridPane gridPane, int row) {
        gridPane.add(this.label, 0, row);
        gridPane.add(this.field, 1, row);
    }

    public ModelAttribute<T> setUserEditable(Boolean userEditable) {
        this.userEditable = userEditable;
        return this;
    }

    public ModelAttribute<T> setDbColumn(Boolean dbColumn) {
        this.dbColumn = dbColumn;
        return this;
    }

    public ModelAttribute<T> setKey(Boolean key) {
        this.key = key;
        return this;
    }

    public ModelAttribute<T> setConverter(Class<? extends StringConverter<T>> converter) {
        this.converter = new NStringConverter<T>(converter);
        this.textProperty.unbindBidirectional(this.property);
        this.textProperty.bindBidirectional(this.property, this.converter);
        return this;
    }

    public Boolean validate() {
        if (!this.userEditable) return true;
        if (this.getValue() == null || this.toString().isEmpty()) {
            field.setStyle("-fx-border-color: red;");
            return false;
        }
        field.setStyle(null);
        return true;
    }

    @Override
    public String toString() {
        return this.textProperty.getValue();
    }

    @Override
    public ModelAttribute<T> clone() {
        return new ModelAttribute<T>(this.getValue(), this.alias, this.type)
            .setDisplayName(this.displayName)
            .setField(this.field.getClass())
            .setUserEditable(this.userEditable)
            .setConverter(this.converter.getSimpleType());
    }
}
