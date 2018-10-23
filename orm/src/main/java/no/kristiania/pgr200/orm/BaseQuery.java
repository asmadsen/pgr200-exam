package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.SqlOperator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

public class BaseQuery<T extends BaseQuery<T>> {
    private final String table;
    private LinkedList<ConditionalStatement> wheres;
    protected int counter = 1;

    public BaseQuery(String table) {
        this.table = table;
        this.wheres = new LinkedList<>();
    }

    protected String getTable() {
        return table;
    }

    public LinkedList<ConditionalStatement> getWheres() {
        return wheres;
    }

    public <V> T where(String key, SqlOperator operator, V value) {
        return this.where(key, operator, value, true);
    }

    public <V> T where(String key, SqlOperator operator, V value, boolean useAnd) {
        this.wheres.add(new ConditionalStatement<>(key, operator, value, useAnd));
        return (T) this;
    }

    public <V> T whereNot(String column, V value) {
        where(column, SqlOperator.Not, value);
        return (T) this;
    }

    public <V> T whereEquals(String column, V value) {
        where(column, SqlOperator.Equals, value);
        return (T) this;
    }

    public T whereIsNull(String column) {
        where(column, SqlOperator.IsNull, null);
        return (T) this;
    }

    public void populateStatement(PreparedStatement statement) throws SQLException {
        for (ConditionalStatement where : this.wheres) {
            if (where.getOperator().hasValue()) {
                statement.setObject(counter++, where.getValue());
            }
        }
    }
}
