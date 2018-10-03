package no.kristiania.pgr200.server.models;

public class Query {

    private final String table;
    private StringBuilder statement;
    private String[] values;

    public Query(String table, String... columns){
        this.table = table;
        this.statement = new StringBuilder().append("SELECT ")
                .append(String.join(", ", columns)).append(" ");
    }

    public Query(String table){
        this.table = table;
        this.statement = new StringBuilder().append("SELECT * ");
    }

    public Query where(String column, Operator operator, String value){
        this.statement.append(column).append(operator.getOperator()).append(value);
        return this;
    }

    public Query whereNotNull(String column){
        this.statement.append(column).append(Operator.Not).append("NULL");
        return this;
    }

    public Query whereNull(String column){
        this.statement.append(column).append(Operator.Equals).append("NULL");
        return this;
    }
}
