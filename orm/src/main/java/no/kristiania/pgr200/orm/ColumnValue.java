package no.kristiania.pgr200.orm;

public class ColumnValue<T> {

    private T value;

    public ColumnValue(T value) {
        this.value = value;
    }

    public Class<?> getType(){
        return value.getClass();
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ColumnValue) {
            return this.getValue().equals(((ColumnValue) obj).getValue());
        }
        return false;
    }
}