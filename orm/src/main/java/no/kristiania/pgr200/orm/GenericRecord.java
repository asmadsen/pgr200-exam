package no.kristiania.pgr200.orm;

import java.util.List;

public class GenericRecord extends BaseRecord {
    @Override
    public String getTable() {
        return null;
    }

    @Override
    public boolean save() {
        return false;
    }

    @Override
    public List all() {
        return null;
    }
}
