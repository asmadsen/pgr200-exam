package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Annotations.Relation;
import no.kristiania.pgr200.orm.Relations.AbstractRelation;
import org.apache.commons.lang3.SerializationUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class BaseRecord<T extends BaseRecord<T, S>, S extends IBaseModel<S>> {
    private S state;
    private S dbState;

    public BaseRecord(Class<S> modelClass) {
        try {
            this.state = modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ignored) {
        }
    }

    public BaseRecord(S state) {
        setState(state);
        if(getState().getAttributes().get(getPrimaryKey()) != null &&
                getState().getAttributes().get(getPrimaryKey()).getValue() != null){
            //setDbState(findById((UUID) getState().getAttributes().get(getPrimaryKey()).getValue()).getState());
        }
    }

    public abstract String getTable();

    public String getPrimaryKey(){
        return "id";
    }

    public T newModelInstance() {
        try {
            return (T) getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean save() {
        if(isDirty()){
            try {
                if (exists()) {
                    if (update()) return true;
                } else {
                    if(create()) return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean update() throws SQLException {
        if(isDirty()){
            UpdateQuery updateQuery = new UpdateQuery(getTable()).whereEquals(getPrimaryKey(),
                    state.getAttribute(getPrimaryKey()).getValue());
            state.getAttributes().forEach((k,v) -> updateQuery.set(k, v.getValue()));
            if (updateQuery.get() > 0) {
                setDbState(SerializationUtils.clone(state));
                return true;
            }
        }
        return false;
    }

    public boolean create() {
        state.setAttribute(getPrimaryKey(), UUID.randomUUID());
        InsertQuery insertQuery = new InsertQuery(getTable()).insert(this);
        try {
            if(insertQuery.get() > 0) {
                setDbState(SerializationUtils.clone(state));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean create(Map<String, ColumnValue> attributes) throws SQLException {
        state.populateAttributes(attributes);
        return create();
    }

    public boolean destroy() {
        if(exists()){
            DeleteQuery deleteQuery = new DeleteQuery(getTable()).whereEquals("id",
                    state.getAttributes().get(getPrimaryKey()).getValue());
            try {
                if(deleteQuery.get() > 0) {
                    setDbState(null);
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public final List<T> all() {
        try {
            return this.newQuery().get();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final T findById(UUID id) {
        try {
            return this.newQuery()
                    .whereEquals("id", id).first();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SelectQuery<T, S> newQuery() throws SQLException {
        return new SelectQuery<>((T) this, state.getAttributes().keySet().toArray(new String[0]));
    }


    public BaseRecord<T, S> fill(Map<String, ColumnValue> attributes){
        this.state.populateAttributes(attributes);
        return this;
    }

    private boolean exists(){
        return getState().getAttributes().get(getPrimaryKey()) != null;
    }

    public boolean isDirty(){
        return !this.getState().equals(getDbState());
    }

    public S getState() {
        return state;
    }

    protected void setState(S state) {
        this.state = state;
    }

    public S getDbState() {
        return dbState;
    }

    public void setDbState(S dbState) {
        this.dbState = dbState;
    }

    public Map<String, AbstractRelation<?, ?, T>> getRelations() {
        Map<String, AbstractRelation<?, ?, T>> relations = new HashMap<>();
        for (Method declaredMethod : getClass().getDeclaredMethods()) {
            if (!declaredMethod.isAnnotationPresent(Relation.class)) continue;
            //if (!declaredMethod.getReturnType().getSuperclass().equals(AbstractRelation.class)) continue;
            try {
                AbstractRelation<?, ?, T> relation = (AbstractRelation<?, ?, T>) declaredMethod.invoke(this);
                relations.put(
                        declaredMethod.getName(),
                        relation
                );
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return relations;
    }
}
