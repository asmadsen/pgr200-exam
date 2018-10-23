package no.kristiania.pgr200.orm;

import org.apache.commons.lang3.SerializationUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class BaseRecord<T extends IBaseModel<T>> {
    private T state;
    private T dbState;

    public abstract String getTable();

    protected boolean save() {
        try {
            if (exists()) {
                if (update()) return true;
            } else {
                if(create()) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean update() throws SQLException {
        if(isDirty()){
            UpdateQuery updateQuery = new UpdateQuery(getTable());
            state.getAttributes().forEach((k,v) -> updateQuery.set(k, v.getValue()));
            PreparedStatement statement = DatabaseConnection.connection.prepareStatement(updateQuery.getSqlStatement());
            updateQuery.populateStatement(statement);
            if (statement.executeUpdate() > 0) {
                setDbState(SerializationUtils.clone(state));
                return true;
            }
        }
        return false;
    }

    private boolean create() throws SQLException {
        state.setPrimaryKey(UUID.randomUUID());
        InsertQuery insertQuery = new InsertQuery(getTable()).insert(this);
        PreparedStatement statement = DatabaseConnection.connection.prepareStatement(insertQuery.getSqlStatement());
        insertQuery.populateStatement(statement);
        if(statement.executeUpdate() > 0) {
            setDbState(SerializationUtils.clone(state));
            return true;
        }
        return false;
    }

    public boolean destroy() throws SQLException {
        DeleteQuery deleteQuery = new DeleteQuery(getTable()).whereEquals("id", state.getPrimaryKey());
        PreparedStatement statement = DatabaseConnection.connection.prepareStatement(deleteQuery.getSqlStatement());
        deleteQuery.populateStatement(statement);
        if(statement.executeUpdate() == 1) {
            setDbState(null);
            return true;
        }
        return false;
    }

    public final List<T> all() throws SQLException {
        ResultSet results = queryStatement(
                new SelectQuery(this.getTable(), state.getAttributes().keySet().toArray(new String[0])));
        List<T> list = new LinkedList<>();
        while (results.next()) list.add(newInstance(results));
        return list;
    }

    public final T findById(UUID id) throws SQLException {
        ResultSet results = queryStatement(
                new SelectQuery(this.getTable(), state.getAttributes().keySet().toArray(new String[0]))
                        .whereEquals("id", id));
        if (results.next()) return newInstance(results);
        return null;
    }

    /**
     * The pointer should be set to the row you wish to use.
     * This is part of the template method findById and all
     * @param resultSet Pointer is set at the row to use, populate the model with this row
     * @return Returns a new instance with values from the resultset
     * @throws SQLException
     */
    protected abstract T newInstance(ResultSet resultSet) throws SQLException;

    private ResultSet queryStatement(SelectQuery query) throws SQLException {
        PreparedStatement preparedStatement = DatabaseConnection.connection.prepareStatement(query.getSqlStatement());
        query.populateStatement(preparedStatement);
        return preparedStatement.executeQuery();
    }

    private boolean exists(){
        return getState().getPrimaryKey() != null;
    }

    public boolean isDirty(){
        return this.getState().compareTo(getDbState()) != 0;
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
