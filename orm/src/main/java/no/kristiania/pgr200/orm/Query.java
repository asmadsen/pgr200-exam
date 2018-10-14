package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.JoinType;
import no.kristiania.pgr200.orm.Enums.OrderDirection;
import no.kristiania.pgr200.orm.Enums.SqlOperator;

import java.util.*;
import java.util.stream.Collectors;

public class Query<T>{

    private final String table;
    private Set<String> selects;
    private LinkedList<ConditionalStatement> wheres, having;
    private LinkedList<JoinStatement> joins;
    private LinkedHashSet<String> groupBy;
    private LinkedHashMap<String, OrderDirection> orderBy;

    private StringJoiner select, from, join, where;
    private List<String> values;

    public Query(String table, String... columns){
        this.table = table;
        this.selects = new LinkedHashSet<>();
        this.groupBy = new LinkedHashSet<>();
        this.orderBy = new LinkedHashMap<>();
        this.select(columns);
        this.select = new StringJoiner(" ").add(Statement.SELECT.getStatement())
                .add(String.join(", ", columns));
    }

    public Query(String table){
        this.table = table;
        from(table);
        this.values = new ArrayList<>();
    }

//    public Query<T> findBy(Class<T> typeClass, int id) {
//        this.select(Arrays.stream(typeClass.getDeclaredMethods())
//                .filter(e -> e.isAnnotationPresent(Record.class) && e.getAnnotation(Record.class).type().equals("SET"))
//                .map(m -> m.getAnnotation(Record.class).column()).toArray(String[]::new));
//        this.where("id", SqlOperator.Equals, String.valueOf(id));
//        return this;
//    }

    public Query<T> select(String... columns){
        Arrays.stream(columns)
              .map(column -> String.format("`%s`", column))
              .forEach(column -> this.selects.add(column));
        return this;
    }

//    public Query<T> join(Class<? extends BaseModel> table, String foreignKey, String localKey, JoinType type){
//        return this;
//    }

    public Query<T> join(Query<T> query, String foreignKey, String localKey, JoinType type){
        return this;
    }

    public <V> Query<T> where(String key, SqlOperator operator, V value){
        return this.where(key, operator, value, false);
    }

    public <V> Query<T> where(String key, SqlOperator operator, V value, boolean or){
        return this;
    }

    public Query<T> having(String key, SqlOperator operator, T value){
        return this;
    }

    public Query<T> groupBy(String... columns){
        Arrays.stream(columns)
              .map(column -> String.format("`%s`", column))
              .forEach(column -> this.groupBy.add(column));
        return this;
    }

    public Query<T> orderBy(String... columns){
        Arrays.stream(columns)
              .forEach(column -> this.orderBy(column, OrderDirection.DESC));
        return this;
    }

    public Query<T> orderBy(String column, OrderDirection direction){
        this.orderBy.put(String.format("`%s`", column), direction);
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

//    public ResultSet execute() throws SQLException {
//        return DatabaseHandling.selectStatement(DatabaseHandling.getConnection().prepareStatement(buildSql()));
//    }

    public StringJoiner initialize(StringJoiner list) {
        if(list == null) return new StringJoiner(" ");
        return list;
    }

    public String getSqlStatement() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format(
                "SELECT %s FROM `%s`",
                String.join(", ", this.selects),
                this.table
        ));
        if (this.wheres != null && this.wheres.size() > 0) {
            // TODO: add where statements
        }
        if (this.groupBy != null && this.groupBy.size() > 0) {
            sql.append(" GROUP BY ")
               .append(String.join(", ", this.groupBy));
        }
        if (this.orderBy != null && this.orderBy.size() > 0) {
            sql.append(" ORDER BY ")
               .append(String.join(
                       ", ",
                       this.orderBy
                               .entrySet()
                               .stream()
                               .map(entry -> String.format("%s %s", entry.getKey(), entry.getValue().name()))
                       .collect(Collectors.toList())
               ));
        }
        return sql.toString();
    }

    public Query<T> count(String column) {
        this.selects.add(String.format("COUNT(`%s`)", column));
        return this;
    }

    public Query<T> average(String column) {
        this.selects.add(String.format("AVG(`%s`)", column));
        return this;
    }

    public Query<T> sum(String column) {
        this.selects.add(String.format("SUM(`%s`)", column));
        return this;
    }

    public Query<T> max(String column) {
        this.selects.add(String.format("MAX(`%s`)", column));
        return this;
    }

    public Query<T> min(String column) {
        this.selects.add(String.format("MIN(`%s`)", column));
        return this;
    }
}
