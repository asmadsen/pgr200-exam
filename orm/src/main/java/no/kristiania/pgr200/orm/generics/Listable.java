package no.kristiania.pgr200.orm.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Listable<T> {
    private T value = null;
    private Collection<T> listValue = null;
    private boolean isList = false;

    public Listable(T value) {
        this.value = value;
    }

    public Listable(Collection<T> listValue) {
        this.listValue = listValue;
        this.isList = true;
    }

    public boolean isList() {
        return this.isList;
    }

    public T getValue() {
        if (!this.isList()) {
            return this.value;
        }
        return null;
    }

    public Collection<T> getListValue() {
        if (this.value != null) {
            return Arrays.asList(this.value);
        } else if (this.listValue != null) {
            return this.listValue;
        }
        return new ArrayList<>();
    }
}
