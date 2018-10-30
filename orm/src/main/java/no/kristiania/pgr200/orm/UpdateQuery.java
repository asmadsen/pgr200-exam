package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.enums.SqlOperator;
import no.kristiania.pgr200.orm.enums.Statement;
import no.kristiania.pgr200.orm.overload_helpers.WhereOverloads;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class UpdateQuery<
        T extends BaseRecord<
                T,
                ? extends IBaseModel<?>>
        >
        implements WhereOverloads<UpdateQuery<T>> {
    private final String table;
    private LinkedList<ConditionalStatement> sets, wheres;

    public UpdateQuery(T model) {
        this.table = model.getTable();
        this.sets = new LinkedList<>();
        this.wheres = new LinkedList<>();
    }

    public <V> UpdateQuery<T> set(String column, V value) {
        sets.add(new ConditionalStatement<>(column, SqlOperator.Equals, value));
        return this;
    }

    public <V> UpdateQuery<T> where(String key, SqlOperator operator, V value, boolean useAnd) {
        this.wheres.add(new ConditionalStatement<>(key, operator, value, useAnd));
        return this;
    }

    protected String getTable() {
        return table;
    }

    String getSqlStatement() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("%s " + Orm.quote + "%s" + Orm.quote, Statement.UPDATE.getStatement(), getTable()));
        if (this.sets.size() > 0) {
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
        PreparedStatement statement = Orm.connection.prepareStatement(getSqlStatement());
        populateStatement(statement);
        return statement.executeUpdate();
    }
}
