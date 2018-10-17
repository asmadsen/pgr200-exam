package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.SqlOperator;
import no.kristiania.pgr200.orm.Enums.Statement;

import java.util.LinkedList;
import java.util.StringJoiner;

public class ConditionalStatement<T> {
    private String key;
    private SqlOperator operator;
    private T value;
    private boolean useAnd;

    public ConditionalStatement(String key, SqlOperator operator, T value) {
        this(key, operator, value, true);
    }

    public ConditionalStatement(String key, SqlOperator operator, T value, boolean useAnd) {
        this.key = key;
        this.operator = operator;
        this.value = value;
        this.useAnd = useAnd;
    }

    public String getSqlStatement() {
        if (this.key.contains(".") && !this.key.matches(".*`[.]`.*"))
            this.key = String.format("%s`.`%s", this.key.split("[.]")[0], this.key.split("[.]")[1]);
        if(!operator.hasValue()) return String.format("`%s` %s", this.key, operator.getOperator());
        return String.format("`%s` %s %s", this.key, operator.getOperator(), "?");
    }

    public String getSqlStatement(String sqlStatement) {
        String andOr = useAnd ? SqlOperator.And.getOperator() : SqlOperator.Or.getOperator();
        StringJoiner statement = new StringJoiner(" ");
        if(sqlStatement != null) statement.add(sqlStatement).add(andOr);
        return statement.add(getSqlStatement()).toString();
    }

    public static String buildStatements(LinkedList<ConditionalStatement> list) {
        String statement = null;
        for (ConditionalStatement statementPart : list) {
            statement = statementPart.getSqlStatement(statement);
        }
        return String.format("%s %s", Statement.WHERE.getStatement(), statement);
    }

    public T getValue() {
        return this.value;
    }

    public SqlOperator getOperator() {
        return operator;
    }
}
