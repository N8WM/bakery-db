package org.bakerydb.util;

import javafx.util.StringConverter;

public class NStringConverter<T> extends StringConverter<T> {
    private Class<StringConverter<T>> simpleType;
    private StringConverter<T> simpleConverter;

    public NStringConverter(StringConverter<T> simpleConverter) {
        super();
        this.simpleConverter = simpleConverter;
    }

    public NStringConverter(
        Class<? extends StringConverter<T>> simpleConverterClass
    ) {
        super();
        try {
            this.simpleConverter = new NStringConverter<T>(
                simpleConverterClass
                    .getDeclaredConstructor()
                    .newInstance()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString(T object) {
        if (object == null) return "";
        return simpleConverter.toString(object);
    }

    @Override
    public T fromString(String string) {
        try {
            return simpleConverter.fromString(string);
        } catch (Exception e) {
            return null;
        }
    }

    public Class<StringConverter<T>> getSimpleType() {
        return simpleType;
    }

    public Boolean isValidStr(String string) {
        try {
            T val = simpleConverter.fromString(string);
            return string.isEmpty() || val != null;
        } catch (Exception e) {
            return false;
        }
    }
}
