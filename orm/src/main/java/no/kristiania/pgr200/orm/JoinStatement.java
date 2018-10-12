package no.kristiania.pgr200.orm;


public class JoinStatement<T> {
    private Class<T> table;
    private Query query;
    private String foreignKey, localKey;
    private JoinType type;
}
