package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.SqlOperator;
import no.kristiania.pgr200.orm.Enums.Statement;
import org.apache.commons.lang3.SerializationUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class BaseRecord<T extends IBaseModel<T>> {
    protected T state;
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
        if (isDirty() && executeStatement(updateStatement(), false) > 0) {
            setDbState(SerializationUtils.clone(state));
            return true;
        }
        return false;
    }

    private boolean create() throws SQLException {
        state.setPrimaryKey(UUID.randomUUID());
        if(executeStatement(insertStatement(), true) > 0) {
            setDbState(SerializationUtils.clone(state));
            return true;
        }
        return false;
    }

    public abstract List<T> all() throws SQLException;

    // TODO: Refactor to be more readable
    protected String insertStatement() {
        StringJoiner values = new StringJoiner(", ");
        state.getAttributes().keySet().forEach(e -> values.add("?"));
        return String.format("%s `%s` (`%s`) %s (%s);",
                Statement.INSERT.getStatement(), getTable(), String.join("`, `", state.getAttributes().keySet()),
                Statement.VALUES, values.toString());
    }

    // TODO: Refactor to be more readable
    protected String updateStatement() {
        StringJoiner columns = new StringJoiner(", ");
        state.getAttributes().entrySet().stream()
                .filter(set -> !set.getKey().equals("id"))
                .forEach(e -> columns.add("`" + e.getKey() + "` " + SqlOperator.Equals.getOperator() + " ?"));
        LinkedList<ConditionalStatement> list = new LinkedList<>();
        list.add(new ConditionalStatement<>("id", SqlOperator.Equals, state.getPrimaryKey()));
        return String.format("%s `%s` %s %s %s;",
                Statement.UPDATE.getStatement(), getTable(),
                Statement.SET.getStatement(), columns.toString(),
                ConditionalStatement.buildStatements(list));
    }

    protected ResultSet queryStatement(Query query) throws SQLException {
        PreparedStatement preparedStatement = DatabaseConnection.connection.prepareStatement(query.getSqlStatement());
        query.populateStatement(preparedStatement);
        return preparedStatement.executeQuery();
    }

    private int executeStatement(String statement, boolean insert) throws SQLException {
        PreparedStatement preparedStatement = DatabaseConnection.connection.prepareStatement(statement);
        int count = 1;
        for (Map.Entry<String, ColumnValue> value : state.getAttributes().entrySet()) {
            if(!insert && value.getKey().equals("id")) continue;
            if (insert && value.getKey().equals("id")) {
                preparedStatement.setObject(count++, value.getValue().getValue());
                continue;
            }
            preparedStatement.setObject(count++, value.getValue().getValue());
        }
        if(!insert) preparedStatement.setObject(count, state.getPrimaryKey());
        return preparedStatement.executeUpdate();
    }

    public boolean exists(){
        return getState().getPrimaryKey() != null;
    }

    public boolean isDirty(){
        return this.getState().compareTo(getDbState()) != 0;
    }

    public T getState() {
        return state;
    }

    public void setState(T state) {
        this.state = state;
    }

    public T getDbState() {
        return dbState;
    }

    public void setDbState(T dbState) {
        this.dbState = dbState;
    }
}
