package no.kristiania.pgr200.orm.test_data;

import java.util.Objects;
import java.util.UUID;

public class Role extends BaseModel<Role> {
    protected UUID id;
    public String name;


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


    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            return this.getId().equals(((User) other).getId()) &&
                    this.getName().equals(((User) other).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
