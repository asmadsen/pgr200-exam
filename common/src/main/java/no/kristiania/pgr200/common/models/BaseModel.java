package no.kristiania.pgr200.common.models;

import no.kristiania.pgr200.common.utils.Utils;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.ColumnValue;
import no.kristiania.pgr200.orm.IBaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static Logger LOGGER = LoggerFactory.getLogger(BaseRecord.class);

    @Override
    public Set<ConstraintViolation<T>> validate() {
        return Utils.validator().validate((T) this);
    }

    @Override
    public T newStateInstance() {
        try {
            return (T) getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("newStateInstance", e);
        }
        return null;
    }

    @Override
    public void populateAttributes(Map<String, ColumnValue> attributes) {
        Class thisClass = getClass();
        for (Map.Entry<String, ColumnValue> entry : attributes.entrySet()) {
            if (entry.getValue() == null) continue;
            try {
                Field field = thisClass.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                if (entry.getValue().getValue() == null) continue;
                if (Utils.primitiveToClassType(field.getType()).equals(entry.getValue().getType())) {
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
            } catch (NoSuchFieldException | IllegalAccessException | SQLException | IOException e) {
                LOGGER.error("populateAttributes", e);
            }
        }
    }

    public T withAttributes(Map<String, ColumnValue> attributes) {
        this.populateAttributes(attributes);
        return (T) this;
    }

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
            } catch (IllegalAccessException e) {
                LOGGER.error("getAttributes", e);
            }
        }
        return attributes;
    }

    @Override
    public void setAttribute(String column, Object value) {
        try {
            getClass().getDeclaredField(column).set(this, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOGGER.error("setAttribute", e);
        }
    }

    @Override
    public ColumnValue getAttribute(String column) {
        try {
            return new ColumnValue<>(getClass().getDeclaredField(column).get(this));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error("getAttribute", e);
        }
        return null;
    }

    public abstract boolean equals(Object other);

    public abstract int hashCode();
}
