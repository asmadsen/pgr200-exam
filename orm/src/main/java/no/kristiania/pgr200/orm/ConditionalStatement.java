package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.enums.SqlOperator;
import no.kristiania.pgr200.orm.enums.Statement;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public class ConditionalStatement<T> {
    private String key;
    private SqlOperator operator;
    private T value;
    private Collection<T> listValue;
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

    public <V extends Collection<T>> ConditionalStatement(String key, SqlOperator operator, V value) {
        this(key, operator, value, true);
    }

    public <V extends Collection<T>> ConditionalStatement(String key, SqlOperator operator, V value, boolean useAnd) {
        this.key = key;
        this.operator = operator;
        this.listValue = value;
        this.useAnd = useAnd;
    }

    public String getSqlStatement() {
        if (this.key.contains(".") && !this.key.matches(".*" + Orm.quote + "[.]" + Orm.quote + ".*"))
            this.key = String.format("%s%s.%s%s",
                                     this.key.split("[.]")[0],
                                     Orm.quote,
                                     Orm.quote,
                                     this.key.split("[.]")[1]);
        if (!operator.hasValue()) return String.format("%s %s", Orm.QuoteString(this.key), operator.getOperator());
        return String.format("%s %s %s", Orm.QuoteString(this.key), operator.getOperator(), this.getValuePlaceholder());
    }

    private String getValuePlaceholder() {
        if (this.operator.equals(SqlOperator.In) && this.getListValue() != null) {
            StringJoiner sj = new StringJoiner(", ");
            for (int i = 0; i < this.getListValue().size(); i++) sj.add("?");
            return "(" + sj.toString() + ")";
        }
        return "?";
    }

    public String getSqlStatement(String sqlStatement) {
        String andOr = useAnd ? SqlOperator.And.getOperator() : SqlOperator.Or.getOperator();
        StringJoiner statement = new StringJoiner(" ");
        if (sqlStatement != null) statement.add(sqlStatement).add(andOr);
        return statement.add(getSqlStatement()).toString();
    }

    public static String buildStatements(List<ConditionalStatement> list) {
        String statement = null;
        for (ConditionalStatement statementPart : list) {
            statement = statementPart.getSqlStatement(statement);
        }
        return String.format("%s %s", Statement.WHERE.getStatement(), statement);
    }

    public T getValue() {
        return this.value;
    }

    public Collection<T> getListValue() {
        if (this.value instanceof Collection) {
            return (Collection<T>) this.value;
        }
        return this.listValue;
    }

    public SqlOperator getOperator() {
        return operator;
    }
}
