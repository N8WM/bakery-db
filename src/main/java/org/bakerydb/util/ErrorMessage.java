package org.bakerydb.util;

public enum ErrorMessage {
    ITEM_NOT_FOUND("Item not found"),
    NO_CONNECTION("No connection to database"),
    NO_KEY("No key returned"),
    NO_CHANGE("No changes made"),
    NO_RESULT("No results found"),
    INTERNAL_ERROR("Internal error"),
    MISSING_KEY("Key missing from result");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public ErrorMessage fromMessage(String message) {
        for (ErrorMessage e : ErrorMessage.values()) {
            if (e.getMessage().equals(message))
                return e;
        }
        throw new IllegalArgumentException("Invalid error message");
    }

    @Override
    public String toString() {
        return this.message;
    }
}
