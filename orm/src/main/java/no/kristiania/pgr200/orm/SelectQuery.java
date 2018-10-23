package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.JoinType;
import no.kristiania.pgr200.orm.Enums.OrderDirection;
import no.kristiania.pgr200.orm.Enums.SqlOperator;

import java.util.*;
import java.util.stream.Collectors;

public class SelectQuery extends BaseQuery<SelectQuery> {

    private Set<String> selects;
    private LinkedList<ConditionalStatement> having;
    private LinkedList<JoinStatement> joins;
    private LinkedHashSet<String> groupBy;
    private LinkedHashMap<String, OrderDirection> orderBy;

    public SelectQuery(String table, String... columns) {
        this(table);
        this.select(columns);
    }

    public SelectQuery(String table) {
        super(table);
        this.selects = new LinkedHashSet<>();
        this.groupBy = new LinkedHashSet<>();
        this.joins = new LinkedList<>();
        this.orderBy = new LinkedHashMap<>();
    }

    public SelectQuery select(String... columns) {
        Arrays.stream(columns)
              .map(column -> String.join(".", Arrays.stream(column.split("\\."))
                                                    .map(part -> String.format("`%s`", part))
                                                    .collect(Collectors.toList()))
              )
              .forEach(column -> this.selects.add(column));
        return this;
    }

//    public SelectQuery<T> join(Class<? extends BaseRecord> table, String foreignKey, String localKey, JoinType type){
//        return this;
//    }

    public <T> SelectQuery having(String key, SqlOperator operator, T value) {
        return this;
    }

    public SelectQuery groupBy(String... columns) {
        Arrays.stream(columns)
              .map(column -> String.format("`%s`", column))
              .forEach(column -> this.groupBy.add(column));
        return this;
    }

    public SelectQuery orderBy(String... columns) {
        Arrays.stream(columns)
              .forEach(column -> this.orderBy(column, OrderDirection.DESC));
        return this;
    }

    public SelectQuery orderBy(String column, OrderDirection direction) {
        this.orderBy.put(String.format("`%s`", column), direction);
        return this;
    }

    public SelectQuery get() {
        return this;
    }

    public <T> T first() {
        return null;
    }

    public String getSqlStatement() {
        StringBuilder sql = new StringBuilder();
        sql.append(String.format(
                "SELECT %s FROM `%s`",
                String.join(", ", this.selects),
                getTable()
        ));
        if (this.joins.size() > 0) {
            this.joins.forEach(join -> {
                sql.append(" ")
                   .append(join.getSqlStatement(getTable()));
            });
        }
        if (getWheres().size() > 0) {
            // TODO: add where statements
            sql.append(" ")
               .append(ConditionalStatement.buildStatements(getWheres()));
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

    public SelectQuery count(String column) {
        this.selects.add(String.format("COUNT(`%s`)", column));
        return this;
    }

    public SelectQuery average(String column) {
        this.selects.add(String.format("AVG(`%s`)", column));
        return this;
    }

    public SelectQuery sum(String column) {
        this.selects.add(String.format("SUM(`%s`)", column));
        return this;
    }

    public SelectQuery max(String column) {
        this.selects.add(String.format("MAX(`%s`)", column));
        return this;
    }

    public SelectQuery min(String column) {
        this.selects.add(String.format("MIN(`%s`)", column));
        return this;
    }

    public SelectQuery join(BaseRecord model, String foreignKey, String localKey) {
        this.joins.add(new JoinStatement<>(model, foreignKey, localKey));
        return this;
    }

    public SelectQuery join(BaseRecord model, String foreignKey, String localKey, JoinType joinType) {
        this.joins.add(new JoinStatement<>(model, foreignKey, localKey, joinType));
        return this;
    }

    public <X extends BaseRecord> SelectQuery join(SelectQuery query, String alias, String foreignKey, String localKey, JoinType joinType) {
        this.joins.add(new JoinStatement<X>(query, alias, foreignKey, localKey, joinType));
        return this;
    }

    public <X extends BaseRecord> SelectQuery join(SelectQuery query, String alias, String foreignKey, String localKey) {
        this.joins.add(new JoinStatement<X>(query, alias, foreignKey, localKey));
        return this;
    }
}
