package no.kristiania.pgr200.orm;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;

public interface IBaseModel<T> {
    Set<ConstraintViolation<T>> validate();
    void populateAttributes(Map<String, ColumnValue> attributes);
    Map<String, ColumnValue> getAttributes();
}
