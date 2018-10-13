package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.BaseModel;

public class UserModel implements BaseModel {
    @Override
    public String getTable() {
        return "users";
    }
}
