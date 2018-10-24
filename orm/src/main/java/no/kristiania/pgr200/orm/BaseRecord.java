package no.kristiania.pgr200.orm;

import org.apache.commons.lang3.SerializationUtils;

import java.sql.SQLException;
import java.util.*;

public abstract class BaseRecord<T extends IBaseModel<T>> {
    private T state;
    private T dbState;

    public abstract String getTable();

    public String getPrimaryKey(){
        return "id";
    }

    protected boolean save() {
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

    private boolean update() throws SQLException {
        if(isDirty()){
            UpdateQuery updateQuery = new UpdateQuery(getTable());
            state.getAttributes().forEach((k,v) -> updateQuery.set(k, v.getValue()));
            if (updateQuery.get() > 0) {
                setDbState(SerializationUtils.clone(state));
                return true;
            }
        }
        return false;
    }

    public boolean create() throws SQLException {
        state.setAttribute(getPrimaryKey(), UUID.randomUUID());
        InsertQuery insertQuery = new InsertQuery(getTable()).insert(this);
        if(insertQuery.get() > 0) {
            setDbState(SerializationUtils.clone(state));
            return true;
        }
        return false;
    }

    public boolean create(Map<String, ColumnValue> attributes) throws SQLException {
        state.populateAttributes(attributes);
        return create();
    }

    public boolean destroy() throws SQLException {
        if(exists()){
            DeleteQuery deleteQuery = new DeleteQuery(getTable()).whereEquals("id",
                    state.getAttributes().get(getPrimaryKey()).getValue());
            if(deleteQuery.get() > 0) {
                setDbState(null);
                return true;
            }
        }
        return false;
    }

    public final List<T> all() throws SQLException {
        return new SelectQuery<>(this, state.getAttributes().keySet().toArray(new String[0])).get();

    }

    public final T findById(UUID id) throws SQLException {
        return new SelectQuery<>(this, state.getAttributes().keySet().toArray(new String[0]))
                .whereEquals("id", id).first();
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
}
