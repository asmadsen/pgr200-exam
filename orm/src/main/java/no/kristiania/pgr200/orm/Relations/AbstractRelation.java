package no.kristiania.pgr200.orm.Relations;

import no.kristiania.pgr200.orm.BaseRecord;

public abstract class AbstractRelation<T extends BaseRecord> {
    protected final String foreignKey;
    protected final String localKey;
    protected Class<T> relationRecord;

    public AbstractRelation(Class<T> relationRecord, String foreignKey, String localKey) {
        this.relationRecord = relationRecord;
        this.foreignKey = foreignKey;
        this.localKey = localKey;
    }

    public Class<T> getRelationRecord() {
        return relationRecord;
    }

    public void setRelationRecord(Class<T> relationRecord) {
        this.relationRecord = relationRecord;
    }
}
