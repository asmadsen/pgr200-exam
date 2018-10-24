package no.kristiania.pgr200.orm.Relations;

import no.kristiania.pgr200.orm.BaseRecord;

public class HasOne<T extends BaseRecord> extends AbstractRelation<T> {

    public HasOne(Class<T> relationRecord, String foreignKey, String localKey) {
        super(relationRecord, foreignKey, localKey);
    }
}
