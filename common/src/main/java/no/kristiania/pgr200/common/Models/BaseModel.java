package no.kristiania.pgr200.common.Models;

import no.kristiania.pgr200.common.Utils.Utils;
import no.kristiania.pgr200.orm.ColumnValue;
import no.kristiania.pgr200.orm.IBaseModel;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BaseModel<T> implements IBaseModel<T> {
    @Override
    public Set<ConstraintViolation<T>> validate() {
        return Utils.validator().validate((T) this);
    }

    @Override
    public void populateAttributes(Map<String, ColumnValue> attributes) {
        Class thisClass = getClass();
        for (Map.Entry<String, ColumnValue> entry : attributes.entrySet()) {
            try {
                Field field = thisClass.getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(this, entry.getValue().getValue());
            } catch (NoSuchFieldException | IllegalAccessException ignored) { }
        }
    }

    @Override
    public Map<String, ColumnValue> getAttributes() {
        Map<String, ColumnValue> attributes = new HashMap<>();
        for (Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                attributes.put(field.getName(), new ColumnValue<>(field.get(this)));
            } catch (IllegalAccessException ignored) { }
        }
        return attributes;
    }
}
