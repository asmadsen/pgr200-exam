package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.ColumnValue;
import no.kristiania.pgr200.orm.IBaseModel;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Role implements IBaseModel<Role> {
    @Override
    public Set<ConstraintViolation<Role>> validate() {
        return null;
    }

    @Override
    public void populateAttributes(Map<String, ColumnValue> attributes) {

    }

    @Override
    public Map<String, ColumnValue> getAttributes() {
        return null;
    }

    @Override
    public void setAttribute(String column, Object value) {

    }

    @Override
    public ColumnValue getAttribute(String column) {
        return null;
    }
}
