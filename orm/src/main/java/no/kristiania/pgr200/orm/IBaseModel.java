package no.kristiania.pgr200.orm;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface IBaseModel<T> {
    Set<ConstraintViolation<T>> validate();
    void populateAttributes(Map<String, ColumnValue> attributes);
    T withAttributes(Map<String, ColumnValue> attributes);
    Map<String, ColumnValue> getAttributes();
    void setAttribute(String column, Object value);
    ColumnValue getAttribute(String column);
    T newStateInstance();
}
