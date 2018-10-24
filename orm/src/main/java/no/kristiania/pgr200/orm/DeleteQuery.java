package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.SqlOperator;
import no.kristiania.pgr200.orm.Enums.Statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

public class DeleteQuery<T> {

    private final String table;
    private LinkedList<ConditionalStatement> wheres;

    public DeleteQuery(String table) {
        this.table = table;
        this.wheres = new LinkedList<>();
    }


    public <V> DeleteQuery<T> where(String key, SqlOperator operator, V value) {
        return this.where(key, operator, value, true);
    }

    public <V> DeleteQuery<T> where(String key, SqlOperator operator, V value, boolean useAnd) {
        this.wheres.add(new ConditionalStatement<>(key, operator, value, useAnd));
        return this;
    }

    public <V> DeleteQuery<T> whereNot(String column, V value) {
        where(column, SqlOperator.Not, value);
        return this;
    }

    public <V> DeleteQuery<T> whereEquals(String column, V value) {
        where(column, SqlOperator.Equals, value);
        return this;
    }

    public DeleteQuery<T> whereIsNull(String column) {
        where(column, SqlOperator.IsNull, null);
        return this;
    }

    protected String getTable() {
        return table;
    }


    public String getSqlStatement() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("%s `%s`", Statement.DELETE.getStatement(), getTable()));
        if (this.wheres.size() > 0) {
            sql.append(" ")
                    .append(ConditionalStatement.buildStatements(this.wheres));
        }
        return sql.toString();
    }

    public int get() throws SQLException {
        PreparedStatement statement = Orm.connection.prepareStatement(getSqlStatement());
        populateStatement(statement);
        return statement.executeUpdate();
    }

    public void populateStatement(PreparedStatement statement) throws SQLException {
        int counter = 1;
        for (ConditionalStatement where : this.wheres) {
            if (where.getOperator().hasValue()) {
                statement.setObject(counter++, where.getValue());
            }
        }
    }
}
