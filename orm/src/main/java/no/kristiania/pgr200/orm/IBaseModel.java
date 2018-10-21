package no.kristiania.pgr200.orm;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IBaseModel<T> extends Comparable<T>, Serializable {
    Set<ConstraintViolation<T>> validate();
    void populateAttributes(Map<String, ColumnValue> attributes);
    Map<String, ColumnValue> getAttributes();
    UUID getPrimaryKey();
    void setPrimaryKey(UUID uuid);
}
