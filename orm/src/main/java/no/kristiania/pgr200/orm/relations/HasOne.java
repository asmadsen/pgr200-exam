package no.kristiania.pgr200.orm.relations;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.IBaseModel;
import no.kristiania.pgr200.orm.generics.Listable;

import java.util.Collection;
import java.util.List;

public class HasOne<V extends BaseRecord<V, W>, W extends IBaseModel<W>, T extends BaseRecord<T, ?>>
        extends HasOneOrMany<V, W, T> {

    public HasOne(V relation, T parent, String foreignKey, String localKey) {
        super(relation, parent, foreignKey, localKey);
    }

    @Override
    public List<T> match(List<T> models, Collection<V> results, String relation) {
        return this.matchOneOrMany(models, results, relation, true);
    }

    @Override
    public Listable<V> getResults() {
        return new Listable<>(this.query.first());
    }
}
