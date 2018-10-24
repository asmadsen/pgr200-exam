package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.ColumnValue;
import no.kristiania.pgr200.orm.IBaseModel;

import javax.validation.ConstraintViolation;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Role extends BaseModel<Role> {
    protected UUID id;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            return this.getId().equals(((User) other).getId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
