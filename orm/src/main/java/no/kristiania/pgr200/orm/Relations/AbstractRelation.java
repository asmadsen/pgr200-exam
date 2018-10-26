package no.kristiania.pgr200.orm.Relations;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.IBaseModel;
import no.kristiania.pgr200.orm.SelectQuery;

import java.util.Collection;

public abstract class AbstractRelation<T extends BaseRecord<T, M>, M extends IBaseModel<M>, P extends BaseRecord<P, ? extends IBaseModel<?>>> {
    protected SelectQuery<T, M> query;
    protected P parent;
    protected T related;

    protected static boolean AddsConstraints = true;


    protected String foreignKey;
    protected String localKey;
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

    abstract public void addConstraints();

    abstract public void addEagerConstraints(Collection<P> models);

    abstract public Collection<P> initRelation(Collection<P> models, String relation);

    abstract public Collection<P> match(Collection<P> models, Collection<T> results, String relation);

    abstract public Collection<T> getResults();

    public Collection<T> getEager() {
        return this.get();
    }

    private Collection<T> get() {
        return null;

        //return this.query.get();
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
