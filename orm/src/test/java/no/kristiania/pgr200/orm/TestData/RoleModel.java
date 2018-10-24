package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.BaseRecord;

public class RoleModel extends BaseRecord<Role> {
    @Override
    public String getTable() {
        return "roles";
    }
}
