package no.kristiania.pgr200.orm.relations;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.IBaseModel;
import no.kristiania.pgr200.orm.SelectQuery;
import no.kristiania.pgr200.orm.generics.Listable;

import java.util.Collection;
import java.util.List;

public abstract class AbstractRelation<T extends BaseRecord<T, M>, M extends IBaseModel<M>, P extends BaseRecord<P, ?>> {
    protected final P parent;
    protected final T related;
    protected SelectQuery<T, M> query;
    protected Class<T> relationRecord;

    public AbstractRelation(SelectQuery<T, M> query, P parent) {
        this.query = query;
        this.related = query.getModel();
        this.parent = parent;
    }

    public Class<T> getRelationRecord() {
        return relationRecord;
    }

    public void setRelationRecord(Class<T> relationRecord) {
        this.relationRecord = relationRecord;
    }

    public abstract void addConstraints();

    public abstract void addEagerConstraints(Collection<P> models);

    public abstract List<P> initRelation(List<P> models, String relation);

    public abstract List<P> match(List<P> models, Collection<T> results, String relation);

    public abstract Listable<T> getResults();

    public Listable<T> getEager() {
        return this.getResults();
    }

    public SelectQuery<T, M> getQuery() {
        return query;
    }

    public P getParent() {
        return parent;
    }

    public T getRelated() {
        return related;
    }
}
