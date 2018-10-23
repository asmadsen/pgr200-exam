package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.SqlOperator;
import no.kristiania.pgr200.orm.Enums.Statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class UpdateQuery extends BaseQuery<UpdateQuery> {
    private LinkedList<ConditionalStatement> sets;

    public UpdateQuery(String table) {
        super(table);
        this.sets = new LinkedList<>();
    }

    public <V> UpdateQuery set(String column, V value){
        sets.add(new ConditionalStatement<>(column, SqlOperator.Equals, value));
        return this;
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
        if (getWheres().size() > 0) {
            sql.append(" ")
                    .append(ConditionalStatement.buildStatements(getWheres())).append(";");
        }
        return sql.toString();
    }

    @Override
    public void populateStatement(PreparedStatement statement) throws SQLException {
        for (ConditionalStatement where : this.sets) {
            if (where.getOperator().hasValue()) {
                statement.setObject(counter++, where.getValue());
            }
        }
        super.populateStatement(statement);
    }
}
