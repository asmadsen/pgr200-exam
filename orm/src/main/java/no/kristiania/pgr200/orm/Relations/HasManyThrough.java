package no.kristiania.pgr200.orm.Relations;

import no.kristiania.pgr200.orm.BaseRecord;

public class HasManyThrough<T extends BaseRecord> extends AbstractRelation<T> {
    private final String pivotTable;
    private final String relationForeignKey;
    private final String relationLocalKey;

    public HasManyThrough(Class<T> relationRecord, String pivotTable, String foreignKey, String localKey, String relationForeignKey, String relationLocalKey) {
        super(relationRecord, foreignKey, localKey);
        this.pivotTable = pivotTable;
        this.relationForeignKey = relationForeignKey;
        this.relationLocalKey = relationLocalKey;
    }
}
