package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.enums.SqlOperator;
import no.kristiania.pgr200.orm.enums.Statement;
import no.kristiania.pgr200.orm.overload_helpers.WhereOverloads;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

public class DeleteQuery<T> implements WhereOverloads<DeleteQuery<T>> {

    private final String table;
    private LinkedList<ConditionalStatement> wheres;

    public DeleteQuery(String table) {
        this.table = table;
        this.wheres = new LinkedList<>();
    }

    public <V> DeleteQuery<T> where(String key, SqlOperator operator, V value, boolean useAnd) {
        this.wheres.add(new ConditionalStatement<>(key, operator, value, useAnd));
        return this;
    }

    protected String getTable() {
        return table;
    }


    public String getSqlStatement() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("%s %s", Statement.DELETE.getStatement(), Orm.QuoteString(getTable())));
        if (!this.wheres.isEmpty()) {
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
