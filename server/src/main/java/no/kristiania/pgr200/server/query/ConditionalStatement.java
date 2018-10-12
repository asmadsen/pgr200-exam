package no.kristiania.pgr200.server.query;

public class ConditionalStatement<T> {
    private String key;
    private SqlOperator operator;
    private T value;
    private boolean useAnd = true;

    public String getStatement(){
        return "";
    }
}
