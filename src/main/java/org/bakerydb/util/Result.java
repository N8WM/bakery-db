package org.bakerydb.util;

import java.util.function.Consumer;

public class Result<T> {
    private final T value;
    private final String error;

    private Result(T value, String error) {
        this.value = value;
        this.error = error;
    }

    public static <T> Result<T> ok(T value) {
        return new Result<>(value, null);
    }

    public static Result<Void> ok() {
        return new Result<>(null, null);
    }

    public static <T> Result<T> err(String error) {
        return new Result<>(null, error);
    }

    public static <T> Result<T> err(ErrorMessage e) {
        return new Result<>(null, e.toString());
    }

    public boolean isOk() {
        return error == null;
    }

    public boolean isErr() {
        return error != null;
    }

    public T getValue() {
        return value;
    }

    public String getError() {
        return error;
    }

    public Result<T> ifOk(Runnable runnable) {
        if (isOk()) {
            runnable.run();
        }
        return this;
    }

    public Result<T> onSuccess(Consumer<T> consumer) {
        if (isOk()) {
            consumer.accept(value);
        }
        return this;
    }

    public Result<T> onSuccess(Runnable runnable) {
        if (isOk()) {
            runnable.run();
        }
        return this;
    }

    public Result<T> onError(Consumer<String> consumer) {
        if (isErr()) {
            consumer.accept(error);
        }
        return this;
    }
}
