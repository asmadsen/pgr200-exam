package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.SqlOperator;
import no.kristiania.pgr200.orm.Enums.Statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class UpdateQuery<T> {
    private final String table;
    private LinkedList<ConditionalStatement> sets, wheres;

    public UpdateQuery(String table) {
        this.table = table;
        this.sets = new LinkedList<>();
        this.wheres = new LinkedList<>();
    }

    public <V> UpdateQuery set(String column, V value){
        sets.add(new ConditionalStatement<>(column, SqlOperator.Equals, value));
        return this;
    }

    public <V> UpdateQuery<T> where(String key, SqlOperator operator, V value) {
        return this.where(key, operator, value, true);
    }

    public <V> UpdateQuery<T> where(String key, SqlOperator operator, V value, boolean useAnd) {
        this.wheres.add(new ConditionalStatement<>(key, operator, value, useAnd));
        return this;
    }

    public <V> UpdateQuery<T> whereNot(String column, V value) {
        where(column, SqlOperator.Not, value);
        return this;
    }

    public <V> UpdateQuery<T> whereEquals(String column, V value) {
        where(column, SqlOperator.Equals, value);
        return this;
    }

    public UpdateQuery<T> whereIsNull(String column) {
        where(column, SqlOperator.IsNull, null);
        return this;
    }

    protected String getTable() {
        return table;
    }

    String getSqlStatement() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("%s `%s`", Statement.UPDATE.getStatement(), getTable()));
        if(this.sets.size() > 0) {
            sql.append(" ").append(Statement.SET.getStatement()).append(" ");
            sql.append(String.join(", ",
                    sets.stream()
                            .map(ConditionalStatement::getSqlStatement)
                            .collect(Collectors.toList())));
        }
        if (this.wheres.size() > 0) {
            sql.append(" ")
                    .append(ConditionalStatement.buildStatements(this.wheres));
        }
        return sql.toString();
    }

    public void populateStatement(PreparedStatement statement) throws SQLException {
        int counter = 1;
        for (ConditionalStatement set : this.sets)
            if (set.getOperator().hasValue()) statement.setObject(counter++, set.getValue());
        for (ConditionalStatement where : this.wheres)
            if (where.getOperator().hasValue()) statement.setObject(counter++, where.getValue());
    }

    public int get() throws SQLException {
        PreparedStatement statement = DatabaseConnection.connection.prepareStatement(getSqlStatement());
        populateStatement(statement);
        return statement.executeUpdate();
    }
}
