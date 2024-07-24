package org.bakerydb.util;

import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;

public class ModelProperty_old<T> extends ObjectPropertyBase<T> {
    private String name = "";
    private Object bean = null;
    private String nameString;
    private String labelString;
    private Class<? extends TextInputControl> fieldType;
    private StringConverter<T> converter;
    private Boolean userEditable;
    private TextInputControl field;
    private Label label;

    public ModelProperty_old(T value) {
        super(value);
    }

    public ModelProperty_old(Object bean, String name) {
        super();
        this.bean = bean;
        this.name = name;
    }

    public ModelProperty_old(Object bean, String name, T value) {
        super(value);
        this.bean = bean;
        this.name = name;
    }

    public void set(
        String name,
        String label,
        Class<? extends TextInputControl> fieldType,
        StringConverter<T> converter,
        Boolean userEditable
    ) {
        this.nameString = name;
        this.labelString = label;
        this.fieldType = fieldType;
        this.converter = converter != null ? converter : new StringConverter<T>() {
            // assume T is a String
            @Override
            public String toString(T object) {
                return object == null ? "" : object.toString();
            }
            @Override
            @SuppressWarnings("unchecked")
            public T fromString(String string) {
                return (T) string;
            }
        };
        this.userEditable = userEditable;
    }

    @Override
    public Object getBean() {
        return this.bean;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getNameString() {
        return this.nameString;
    }

    public Boolean isUserEditable() {
        return this.userEditable;
    }

    private void constructField() {
        if (field != null)
            return;

        try {
            field = fieldType.getDeclaredConstructor().newInstance();
            field.setPromptText(nameString);
            field.setPromptText(name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        field.textProperty().set(this.converter.toString(this.get()));
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty())
                return;
            try {
                this.set(this.converter.fromString(newValue));
            } catch (Exception e) {
                field.setText(oldValue);
            }
        });
    }

    private void constructLabel(TextInputControl field) {
        if (label != null)
            return;

        label = new Label(this.labelString);
        label.setMnemonicParsing(true);
        label.setLabelFor(field);
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setTextAlignment(TextAlignment.RIGHT);
        label.setPadding(new Insets(0, 0, 0, 10));
        label.setWrapText(true);
        label.setPrefWidth(100);
        label.setMaxWidth(100);
    }

    public void addFieldTo(GridPane grid, int row) {
        if (field != null || label != null)
            return;

        constructField();
        constructLabel(field);
        grid.add(label, 0, row);
        grid.add(field, 1, row);
    }

    @Override
    public String toString() {
        T val = get();
        return (val == null) ? "" : val.toString();
    }

    public boolean isEmpty() {
        T val = this.get();
        return val == null || val.toString().isEmpty() || val.toString().isBlank();
    }

    public boolean hasValue() {
        return !isEmpty();
    }

    public boolean validate() {
        if (field == null)
            return true;

        if (isEmpty()) {
            field.setStyle("-fx-border-color: red;"); 
            return false;
        } else {
            field.setStyle(null);
            return true;
        }
    }
}
