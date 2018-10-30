package no.kristiania.pgr200.orm.relations;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.IBaseModel;
import no.kristiania.pgr200.orm.enums.SqlOperator;
import no.kristiania.pgr200.orm.generics.Listable;

import java.util.*;
import java.util.stream.Collectors;

public class BelongsTo<V extends BaseRecord<V, W>, W extends IBaseModel<W>, T extends BaseRecord<T, ?>>
        extends AbstractRelation<V, W, T> {
    private final String foreignKey;
    private final String ownerKey;

    public BelongsTo(V relation, T parent, String foreignKey, String ownerKey) {
        super(relation.newQuery(), parent);
        this.foreignKey = foreignKey;
        this.ownerKey = ownerKey;
    }

    @Override
    public void addConstraints() {
        this.query.where(this.ownerKey, SqlOperator.Equals, this.parent.getColumnValue(this.foreignKey).getValue())
                  .whereNotNull(this.ownerKey);
    }

    @Override
    public void addEagerConstraints(Collection<T> models) {
        this.query.where(this.ownerKey,
                         SqlOperator.In,
                         models.stream()
                               .map(m -> m.getState().getAttribute(this.foreignKey).getValue())
                               .collect(Collectors.toList()));
    }

    @Override
    public List<T> initRelation(List<T> models, String relation) {
        for (T model : models) {
            model.setRelation(relation, null);
        }
        return models;
    }

    @Override
    public List<T> match(List<T> models, Collection<V> results, String relation) {
        Map<Integer, V> resultMap = new HashMap<>();
        for (V result : results) {
            resultMap.put(Objects.hash(result.getState().getAttribute(this.ownerKey).getValue()), result);
        }

        for (T model : models) {
            V result = resultMap.get(Objects.hash(model.getState().getAttribute(this.foreignKey).getValue()));
            if (result != null) {
                model.setRelation(relation, result);
            }
        }
        return models;
    }

    @Override
    public Listable<V> getResults() {
        return new Listable<>(this.query.first());
    }
}
