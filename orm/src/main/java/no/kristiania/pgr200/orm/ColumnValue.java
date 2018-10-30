package no.kristiania.pgr200.orm;

import java.util.Objects;

public class ColumnValue<T> {

    private T value;

    public ColumnValue(T value) {
        this.value = value;
    }

    public Class<?> getType() {
        return value.getClass();
    }

    public T getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ColumnValue) {
            return this.getValue().equals(((ColumnValue) obj).getValue());
        }
        return false;
    }
}