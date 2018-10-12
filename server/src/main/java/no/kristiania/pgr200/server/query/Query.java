package no.kristiania.pgr200.server.query;

import no.kristiania.pgr200.server.annotations.Record;
import no.kristiania.pgr200.server.db.DatabaseHandling;
import no.kristiania.pgr200.server.models.BaseModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Query<T>{

    private final String table;
    private Set<String> selects;
    private LinkedList<ConditionalStatement> wheres, having;
    private LinkedList<JoinStatement> joins;
    private LinkedHashSet<String> groupBy, orderBy;

    private StringJoiner select, from, join, where;
    private List<String> values;

    public Query(String table, String... columns){
        this.table = table;
        this.select = new StringJoiner(" ").add(Statement.SELECT.getStatement())
                .add(String.join(", ", columns));
    }

    public Query(String table){
        this.table = table;
        from(table);
        this.values = new ArrayList<>();
    }

    public Query<T> findBy(Class<T> typeClass, int id) {
        this.select(Arrays.stream(typeClass.getDeclaredMethods())
                .filter(e -> e.isAnnotationPresent(Record.class) && e.getAnnotation(Record.class).type().equals("SET"))
                .map(m -> m.getAnnotation(Record.class).column()).toArray(String[]::new));
        this.where("id", SqlOperator.Equals, String.valueOf(id));
        return this;
    }

    public Query<T> select(String... columns){
        return this;
    }

    public Query<T> join(Class<? extends BaseModel> table, String foreignKey, String localKey, JoinType type){
        return this;
    }

    public Query<T> join(Query<T> query, String foreignKey, String localKey, JoinType type){
        return this;
    }

    public <V> Query<T> where(String key, SqlOperator operator, V value){
        return this;
    }

    public Query<T> having(String key, SqlOperator operator, T value){
        return this;
    }

    public Query<T> groupBy(String... columns){
        return this;
    }

    public Query<T> orderBy(String... columns){
        return this;
    }

    public Query<T> get(){
        return this;
    }

    public Query<T> first(){
        return this;
    }

    public Query<T> delete(){
        return this;
    }

//    public <V, K> Query<T> insert(Map<K, V> columns){
//        initialize(insert).add(Statement.INSERT.getStatement()).add(table);
//        columns.forEach((k, v) -> {
//        });
//        for (Map.Entry column : columns) {
//            initialize(insert).add(Statement.INSERT).add(column.getKey());
//        }
//        return this;
//    }

    public Query<T> whereNot(String column, String value){
        where(column, SqlOperator.Not, value);
        return this;
    }

    public Query<T> whereEquals(String column, String value){
        where(column, SqlOperator.Equals, value);
        return this;
    }

    public Query<T> whereIsNull(String column){
        where(column, SqlOperator.IsNull, null);
        return this;
    }

    private Query<T> from(String table){
        this.from = initialize(from);
        this.from.add(Statement.FROM.getStatement()).add(table);
        return this;
    }

    public String buildSql(){
        if(select.length() == 0) this.select("*");
        StringJoiner query = new StringJoiner(" ");
        query.add(select.toString());
        query.add(from.toString());
        if(where != null) query.add(Statement.WHERE.getStatement()).add(where.toString());
        return query.toString() + ";";
    }

    public ResultSet execute() throws SQLException {
        return DatabaseHandling.selectStatement(DatabaseHandling.getConnection().prepareStatement(buildSql()));
    }

    public StringJoiner initialize(StringJoiner list) {
        if(list == null) return new StringJoiner(" ");
        return list;
    }
}
