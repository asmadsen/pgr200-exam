package no.kristiania.pgr200.orm.test_data;

import no.kristiania.pgr200.orm.ColumnValue;
import no.kristiania.pgr200.orm.IBaseModel;

import javax.validation.ConstraintViolation;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseModel<T> implements IBaseModel<T> {
    @Override
    public Set<ConstraintViolation<T>> validate() {
        return null;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public T newStateInstance() {
        try {
            return (T) getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void populateAttributes(Map<String, ColumnValue> attributes) {
        Class thisClass = getClass();
        for (Map.Entry<String, ColumnValue> entry : attributes.entrySet()) {
            if (entry.getValue() == null) continue;
            try {
                Field field = thisClass.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                if (entry.getValue().getValue() == null) continue;
                if (field.getType().equals(entry.getValue().getType())) {
                    field.set(this, entry.getValue().getValue());
                } else if (entry.getValue().getValue() instanceof Clob) {
                    StringBuilder sb = new StringBuilder();
                    Reader reader = ((Clob) entry.getValue().getValue()).getCharacterStream();
                    int c;
                    while ((c = reader.read()) != -1) {
                        sb.append((char) c);
                    }
                    field.set(this, sb.toString());
                }
            } catch (NoSuchFieldException | IllegalAccessException | SQLException | IOException ignored) {
            }
        }
    }

    public T withAttributes(Map<String, ColumnValue> attributes) {
        this.populateAttributes(attributes);
        return (T) this;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public Map<String, ColumnValue> getAttributes() {
        Map<String, ColumnValue> attributes = new HashMap<>();
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isSynthetic()) continue;
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value != null) value = new ColumnValue<>(field.get(this));
                attributes.put(field.getName(), (ColumnValue) value);
            } catch (IllegalAccessException ignored) {
            }
        }
        return attributes;
    }

    @Override
    public void setAttribute(String column, Object value) {
        try {
            getClass().getDeclaredField(column).set(this, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ColumnValue getAttribute(String column) {
        try {
            return new ColumnValue<>(getClass().getDeclaredField(column).get(this));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract boolean equals(Object other);

    public abstract int hashCode();
}
