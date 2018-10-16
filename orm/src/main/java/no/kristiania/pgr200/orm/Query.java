package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.JoinType;
import no.kristiania.pgr200.orm.Enums.OrderDirection;
import no.kristiania.pgr200.orm.Enums.SqlOperator;

import java.util.*;
import java.util.stream.Collectors;

public class Query<T> {

    private final String table;
    private Set<String> selects;
    private LinkedList<ConditionalStatement> wheres, having;
    private LinkedList<JoinStatement> joins;
    private LinkedHashSet<String> groupBy;
    private LinkedHashMap<String, OrderDirection> orderBy;

    public Query(String table, String... columns) {
        this(table);
        this.select(columns);
    }

    public Query(String table) {
        this.table = table;
        this.selects = new LinkedHashSet<>();
        this.groupBy = new LinkedHashSet<>();
        this.wheres = new LinkedList<>();
        this.joins = new LinkedList<>();
        this.orderBy = new LinkedHashMap<>();
    }

//    public Query<T> findBy(Class<T> typeClass, int id) {
//        this.select(Arrays.stream(typeClass.getDeclaredMethods())
//                .filter(e -> e.isAnnotationPresent(Record.class) && e.getAnnotation(Record.class).type().equals("SET"))
//                .map(m -> m.getAnnotation(Record.class).column()).toArray(String[]::new));
//        this.where("id", SqlOperator.Equals, String.valueOf(id));
//        return this;
//    }

    public Query<T> select(String... columns) {
        Arrays.stream(columns)
              .map(column -> String.join(".", Arrays.stream(column.split("\\."))
                                                    .map(part -> String.format("`%s`", part))
                                                    .collect(Collectors.toList()))
              )
              .forEach(column -> this.selects.add(column));
        return this;
    }

//    public Query<T> join(Class<? extends BaseModel> table, String foreignKey, String localKey, JoinType type){
//        return this;
//    }



    public <V> Query<T> where(String key, SqlOperator operator, V value) {
        return this.where(key, operator, value, true);
    }

    public <V> Query<T> where(String key, SqlOperator operator, V value, boolean useAnd) {
        this.wheres.add(new ConditionalStatement<>(key, operator, value, useAnd));
        return this;
    }

    public Query<T> having(String key, SqlOperator operator, T value) {
        return this;
    }

    public Query<T> groupBy(String... columns) {
        Arrays.stream(columns)
              .map(column -> String.format("`%s`", column))
              .forEach(column -> this.groupBy.add(column));
        return this;
    }

    public Query<T> orderBy(String... columns) {
        Arrays.stream(columns)
              .forEach(column -> this.orderBy(column, OrderDirection.DESC));
        return this;
    }

    public Query<T> orderBy(String column, OrderDirection direction) {
        this.orderBy.put(String.format("`%s`", column), direction);
        return this;
    }

    public Query<T> get() {
        return this;
    }

    public T first() {
        return null;
    }

    public Query<T> delete() {
        return this;
    }

    public <V> Query<T> whereNot(String column, V value) {
        where(column, SqlOperator.Not, value);
        return this;
    }

    public <V> Query<T> whereEquals(String column, V value) {
        where(column, SqlOperator.Equals, value);
        return this;
    }

    public Query<T> whereIsNull(String column) {
        where(column, SqlOperator.IsNull, null);
        return this;
    }

//    public ResultSet execute() throws SQLException {
//        return DatabaseHandling.selectStatement(DatabaseHandling.getConnection().prepareStatement(buildSql()));
//    }

    public String getSqlStatement() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format(
                "SELECT %s FROM `%s`",
                String.join(", ", this.selects),
                this.table
        ));
        if (this.joins.size() > 0) {
            this.joins.forEach(join -> {
                sql.append(" ")
                   .append(join.getSqlStatement(this.table));
            });
        }
        if (this.wheres.size() > 0) {
            // TODO: add where statements
            sql.append(" ")
               .append(ConditionalStatement.buildStatements(this.wheres));
        }
        if (this.groupBy.size() > 0) {
            sql.append(" GROUP BY ")
               .append(String.join(", ", this.groupBy));
        }
        if (this.orderBy.size() > 0) {
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

    public Query<T> join(BaseModel model, String foreignKey, String localKey) {
        this.joins.add(new JoinStatement<>(model, foreignKey, localKey));
        return this;
    }

    public Query<T> join(BaseModel model, String foreignKey, String localKey, JoinType joinType) {
        this.joins.add(new JoinStatement<>(model, foreignKey, localKey, joinType));
        return this;
    }

    public <X extends BaseModel> Query<T> join(Query<X> query, String alias, String foreignKey, String localKey, JoinType joinType) {
        this.joins.add(new JoinStatement<X>(query, alias, foreignKey, localKey, joinType));
        return this;
    }

    public <X extends BaseModel> Query<T> join(Query<X> query, String alias, String foreignKey, String localKey) {
        this.joins.add(new JoinStatement<X>(query, alias, foreignKey, localKey));
        return this;
    }
}
