package no.kristiania.pgr200.orm.Relations;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.IBaseModel;
import no.kristiania.pgr200.orm.SelectQuery;

import java.util.Collection;

public class BelongsTo<T extends BaseRecord<T, M>, M extends IBaseModel<M>, P extends BaseRecord<P, ? extends IBaseModel<?>>> extends AbstractRelation<T, M, P> {
    private final String foreignKey;
    private final String ownerKey;
    private final String relation;

    public BelongsTo(SelectQuery<T, M> query, P parent, String foreignKey, String ownerKey, String relation) {
        super(query, parent);
        this.foreignKey = foreignKey;
        this.ownerKey = ownerKey;
        this.relation = relation;
    }

    @Override
    public void addConstraints() {

    }

    @Override
    public void addEagerConstraints(Collection<P> models) {

    }

    @Override
    public Collection<P> initRelation(Collection<P> models, String relation) {
        return null;
    }

    @Override
    public Collection<P> match(Collection<P> models, Collection<T> results, String relation) {
        return null;
    }

    @Override
    public Collection<T> getResults() {
        return null;
    }
}
