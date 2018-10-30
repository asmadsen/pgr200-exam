package no.kristiania.pgr200.orm.test_data;

import no.kristiania.pgr200.orm.BaseRecord;

public class RoleModel extends BaseRecord<RoleModel, Role> {
    public RoleModel() {
        super(new Role());
    }

    @Override
    public String getTable() {
        return "roles";
    }
}
