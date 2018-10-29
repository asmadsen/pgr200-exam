package no.kristiania.pgr200.orm.Relations;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.Enums.SqlOperator;
import no.kristiania.pgr200.orm.Generics.Listable;
import no.kristiania.pgr200.orm.IBaseModel;
import no.kristiania.pgr200.orm.SelectQuery;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Collection;
import java.util.List;

public abstract class AbstractRelation<T extends BaseRecord<T, M>, M extends IBaseModel<M>, P extends BaseRecord<P, ?>> {
    protected SelectQuery<T, M> query;
    protected final P parent;
    protected final T related;

    protected static boolean AddsConstraints = true;

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

    abstract public List<P> initRelation(List<P> models, String relation);

    abstract public List<P> match(List<P> models, Collection<T> results, String relation);

    abstract public Listable<T> getResults();

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
