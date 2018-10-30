package no.kristiania.pgr200.orm.relations;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.IBaseModel;
import no.kristiania.pgr200.orm.enums.SqlOperator;

import java.util.*;
import java.util.stream.Collectors;

public abstract class HasOneOrMany<V extends BaseRecord<V, W>, W extends IBaseModel<W>, T extends BaseRecord<T, ?>>
        extends AbstractRelation<V, W, T> {
    private final String foreignKey;
    private final String localKey;

    public HasOneOrMany(V relation, T parent, String foreignKey, String localKey) {
        super(relation.newQuery(), parent);
        this.foreignKey = foreignKey;
        this.localKey = localKey;
    }

    @Override
    public void addConstraints() {
        this.query.where(this.foreignKey, SqlOperator.Equals, parent.getColumnValue(this.localKey).getValue())
                  .whereNotNull(this.foreignKey);
    }

    @Override
    public void addEagerConstraints(Collection<T> models) {
        this.query.where(
                this.foreignKey,
                SqlOperator.In,
                models.stream()
                      .map(m -> m.getState().getAttribute(this.localKey).getValue())
                      .collect(Collectors.toList())
        );
    }

    @Override
    public List<T> initRelation(List<T> models, String relation) {
        for (T model : models) {
            model.setRelation(relation, null);
        }
        return models;
    }

    protected List<T> matchOneOrMany(List<T> models, Collection<V> results, String relation, boolean one) {
        Map<Integer, ArrayList<V>> resultMap = new HashMap<>();
        for (V result : results) {
            int hash = Objects.hash(result.getState().getAttribute(this.foreignKey).getValue());
            resultMap.putIfAbsent(hash, new ArrayList<V>());
            resultMap.get(hash).add(result);
        }

        for (T model : models) {
            List<V> result = resultMap.get(Objects.hash(model.getState().getAttribute(this.localKey).getValue()));
            if (result != null) {
                if (one) {
                    model.setRelation(relation, result.get(0));
                } else {
                    model.setRelation(relation, result);
                }
            }
        }
        return models;
    }
}
