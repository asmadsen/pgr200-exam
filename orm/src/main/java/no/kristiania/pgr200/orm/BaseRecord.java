package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.SqlOperator;
import no.kristiania.pgr200.orm.Enums.Statement;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public abstract class BaseRecord<T extends IBaseModel<T>> {
    protected IBaseModel state;
    private BaseRecord dbState;
    private UUID primaryKey;
    protected T model;

    public abstract String getTable();

    protected boolean save() {
        try {
            String statement;
            if (!exists()) {
                statement = insertStatement();
                if(executeStatement(statement, true) > 0) return true;
            } else {
                statement = updateStatement();
                if(executeStatement(statement, false) > 0){
                    return true;
                } else {
                    statement = insertStatement();
                    if(executeStatement(statement, true) > 0) return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean create(){
        return false;
    }


    // TODO: Refactor to be more readable
    protected String insertStatement() {
        StringJoiner values = new StringJoiner(", ");
        model.getAttributes().keySet().forEach(e -> values.add("?"));
        return String.format("%s `%s` (`%s`) %s (%s);",
                Statement.INSERT.getStatement(), getTable(), String.join("`, `", model.getAttributes().keySet()),
                Statement.VALUES, values.toString());
    }

    // TODO: Refactor to be more readable
    protected String updateStatement() {
        StringJoiner columns = new StringJoiner(", ");
        model.getAttributes().entrySet().stream()
                .filter(set -> !set.getKey().equals("id"))
                .forEach(e -> columns.add("`" + e.getKey() + "` " + SqlOperator.Equals.getOperator() + " ?"));
        LinkedList<ConditionalStatement> list = new LinkedList<>();
        list.add(new ConditionalStatement<>("id", SqlOperator.Equals, model.getAttributes().get("id").getValue()));
        return String.format("%s `%s` %s %s %s;",
                Statement.UPDATE.getStatement(), getTable(),
                Statement.SET.getStatement(), columns.toString(),
                ConditionalStatement.buildStatements(list));
    }

    private int executeStatement(String statement, boolean insert) throws SQLException {
        PreparedStatement preparedStatement = DatabaseConnection.connection.prepareStatement(statement);
        int count = 1;
        for (Map.Entry<String, ColumnValue> value : model.getAttributes().entrySet()) {
            if(!insert && value.getKey().equals("id")) continue;
            if (insert && value.getKey().equals("id")) {
                preparedStatement.setObject(count++, UUID.randomUUID());
                continue;
            }
            preparedStatement.setObject(count++, value.getValue().getValue());
        }
        if(!insert) preparedStatement.setObject(count, model.getAttributes().get("id").getValue());
        return preparedStatement.executeUpdate();
    }

    public boolean exists(){
        return false;
    }
}
