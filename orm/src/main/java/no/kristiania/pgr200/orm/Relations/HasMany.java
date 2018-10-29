package no.kristiania.pgr200.orm.Relations;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.Generics.Listable;
import no.kristiania.pgr200.orm.IBaseModel;

import java.util.*;

public class HasMany<V extends BaseRecord<V, W>, W extends IBaseModel<W>, T extends BaseRecord<T, ?>> extends HasOneOrMany<V, W, T> {

    public HasMany(V relation, T parent, String foreignKey, String localKey) {
        super(relation, parent, foreignKey, localKey);
    }

    @Override
    public List<T> match(List<T> models, Collection<V> results, String relation) {
        return this.matchOneOrMany(models, results, relation, false);
    }

    @Override
    public Listable<V> getResults() {
        return new Listable<>(this.query.get());
    }
}
