package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Annotations.Relation;
import no.kristiania.pgr200.orm.Relations.AbstractRelation;
import no.kristiania.pgr200.orm.Relations.BelongsTo;
import no.kristiania.pgr200.orm.Relations.HasMany;
import no.kristiania.pgr200.orm.Relations.HasOne;
import no.kristiania.pgr200.orm.Utils.RecordUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.lang3.SerializationUtils;

import java.sql.SQLException;
import java.util.*;

public abstract class BaseRecord<T extends IBaseModel<T>> {
    private T state;
    private T dbState;

    public BaseRecord(T state) {
        setState(state);
        if(getState().getAttributes().get(getPrimaryKey()) != null &&
                getState().getAttributes().get(getPrimaryKey()).getValue() != null){
            setDbState(findById((UUID) getState().getAttributes().get(getPrimaryKey()).getValue()));
        }
    }

    public abstract String getTable();

    public String getPrimaryKey(){
        return "id";
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
            return new SelectQuery<>(this, state.getAttributes().keySet().toArray(new String[0])).get();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final T findById(UUID id) {
        try {
            return new SelectQuery<>(this, state.getAttributes().keySet().toArray(new String[0]))
                    .whereEquals("id", id).first();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public T fill(Map<String, ColumnValue> attributes){
        state.populateAttributes(attributes);
        return state;
    }

    private boolean exists(){
        return getState().getAttributes().get(getPrimaryKey()) != null;
    }

    public boolean isDirty(){
        return !this.getState().equals(getDbState());
    }

    public T getState() {
        return state;
    }

    protected void setState(T state) {
        this.state = state;
    }

    public T getDbState() {
        return dbState;
    }

    public void setDbState(T dbState) {
        this.dbState = dbState;
    }

    public Map<String, AbstractRelation> getRelations() {
        Map<String, AbstractRelation> relations = new HashMap<>();
        for (Method declaredMethod : getClass().getDeclaredMethods()) {
            if (!declaredMethod.isAnnotationPresent(Relation.class)) continue;
            if (!declaredMethod.getReturnType().getSuperclass().equals(AbstractRelation.class)) continue;
            try {
                AbstractRelation<? extends BaseRecord> relation = (AbstractRelation<? extends BaseRecord>) declaredMethod.invoke(this);
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

    protected <V extends BaseRecord> HasOne<V> hasOne(Class<V> modelClass) {
        String primaryKey = this.getPrimaryKey();
        String foreignKey = RecordUtils.GuessForeignKey(this, primaryKey);
        return this.hasOne(modelClass, foreignKey, primaryKey);
    }

    protected <V extends BaseRecord> HasOne<V> hasOne(Class<V> modelClass, String foreignKey) {
        String primaryKey = this.getPrimaryKey();
        return this.hasOne(modelClass, foreignKey, primaryKey);
    }

    protected <V extends BaseRecord> HasOne<V> hasOne(Class<V> modelClass, String foreignKey, String localKey) {
        return new HasOne<>(modelClass, foreignKey, localKey);
    }

    protected <V extends BaseRecord> BelongsTo<V> belongsTo(Class<V> modelClass) {
        try {
            V model = modelClass.newInstance();
            String primaryKey = model.getPrimaryKey();
            String foreignKey = RecordUtils.GuessForeignKey(model, primaryKey);
            return this.belongsTo(modelClass, foreignKey, primaryKey);
        } catch (InstantiationException | IllegalAccessException ignored) {}
        return null;
    }

    protected <V extends BaseRecord> BelongsTo<V> belongsTo(Class<V> modelClass, String foreignKey) {
        try {
            V model = modelClass.newInstance();
            String primaryKey = model.getPrimaryKey();
            return this.belongsTo(modelClass, foreignKey, primaryKey);
        } catch (InstantiationException | IllegalAccessException ignored) {}
        return null;
    }

    protected <V extends BaseRecord> BelongsTo<V> belongsTo(Class<V> modelClass, String foreignKey, String localKey) {
        return new BelongsTo<>(modelClass, foreignKey, localKey);
    }

    protected <V extends BaseRecord> HasMany<V> hasMany(Class<V> modelClass) {
        String primaryKey = this.getPrimaryKey();
        String foreignKey = RecordUtils.GuessForeignKey(this, primaryKey);
        return this.hasMany(modelClass, foreignKey, primaryKey);
    }

    protected <V extends BaseRecord> HasMany<V> hasMany(Class<V> modelClass, String foreignKey) {
        String primaryKey = this.getPrimaryKey();
        return this.hasMany(modelClass, foreignKey, primaryKey);
    }

    protected <V extends BaseRecord> HasMany<V> hasMany(Class<V> modelClass, String foreignKey, String localKey) {
        return new HasMany<>(modelClass, foreignKey, localKey);
    }
}
