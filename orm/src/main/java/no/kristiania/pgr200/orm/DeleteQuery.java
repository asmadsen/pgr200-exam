package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.Statement;

public class DeleteQuery extends BaseQuery<DeleteQuery>{

    public DeleteQuery(String table) {
        super(table);
    }

    public String getSqlStatement() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format("%s `%s`", Statement.DELETE.getStatement(), getTable()));
        if (getWheres().size() > 0) {
            sql.append(" ")
                    .append(ConditionalStatement.buildStatements(getWheres())).append(";");
        }
        return sql.toString();
    }
}
