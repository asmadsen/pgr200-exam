package no.kristiania.pgr200.orm.TestData;

import java.util.*;

public class User extends BaseModel<User> {

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
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return this.getId().equals(((User) obj).getId()) &&
                    this.getEmail().equals(((User) obj).getEmail()) &&
                    this.getName().equals(((User) obj).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
