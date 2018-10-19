package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.ColumnValue;
import no.kristiania.pgr200.orm.IBaseModel;

import javax.validation.ConstraintViolation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class User implements IBaseModel<User> {

    protected UUID id;
    public String name;
    public String email;

    public User() {
    }

    public User(UUID id, String name, String email){
        setId(id);
        setName(name);
        setEmail(email);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public Set<ConstraintViolation<User>> validate() {
        return null;
    }

    @SuppressWarnings("Duplicates")
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
            if(field.isSynthetic()) continue;
            field.setAccessible(true);
            try {
                attributes.put(field.getName(), new ColumnValue<>(field.get(this)));
            } catch (IllegalAccessException ignored) { }
        }
        return attributes;
    }
}
