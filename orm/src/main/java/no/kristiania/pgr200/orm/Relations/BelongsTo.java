package no.kristiania.pgr200.orm.Relations;

import no.kristiania.pgr200.orm.BaseRecord;

public class BelongsTo<T extends BaseRecord> extends AbstractRelation<T> {
    public BelongsTo(Class<T> relationRecord, String foreignKey, String localKey) {
        super(relationRecord, foreignKey, localKey);
    }
}
