package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.JoinType;
import no.kristiania.pgr200.orm.Enums.OrderDirection;
import no.kristiania.pgr200.orm.Enums.SqlOperator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SelectQuery<T extends BaseRecord<V>, V extends IBaseModel<V>> {
    private final String table;
    private T model;
    private Set<String> selects;
    private LinkedList<ConditionalStatement> wheres, having;
    private LinkedList<JoinStatement> joins;
    private LinkedHashSet<String> groupBy;
    private LinkedHashMap<String, OrderDirection> orderBy;
    private int limit;

    public SelectQuery(T model, String... columns) {
        this(model);
        this.select(columns);
    }

    public SelectQuery(T model) {
        this.model = model;
        this.table = model.getTable();
        this.selects = new LinkedHashSet<>();
        this.wheres = new LinkedList<>();
        this.groupBy = new LinkedHashSet<>();
        this.joins = new LinkedList<>();
        this.orderBy = new LinkedHashMap<>();
    }

    public SelectQuery<T, V> select(String... columns) {
        Arrays.stream(columns)
              .map(column -> String.join(".", Arrays.stream(column.split("\\."))
                                                    .map(part -> String.format("`%s`", part))
                                                    .collect(Collectors.toList()))
              )
              .forEach(column -> this.selects.add(column));
        return this;
    }

    protected String getTable() {
        return table;
    }

    public LinkedList<ConditionalStatement> getWheres() {
        return wheres;
    }

    public void populateStatement(PreparedStatement statement) throws SQLException {
        int counter = 1;
        for (ConditionalStatement where : this.wheres) {
            if (where.getOperator().hasValue()) {
                statement.setObject(counter++, where.getValue());
            }
        }
    }

    public <X> SelectQuery<T, V> where(String key, SqlOperator operator, X value) {
        return this.where(key, operator, value, true);
    }

    public <X> SelectQuery<T, V> where(String key, SqlOperator operator, X value, boolean useAnd) {
        this.wheres.add(new ConditionalStatement<>(key, operator, value, useAnd));
        return this;
    }

    public <X> SelectQuery<T, V> whereNot(String column, X value) {
        where(column, SqlOperator.Not, value);
        return this;
    }

    public <X> SelectQuery<T, V> whereEquals(String column, X value) {
        where(column, SqlOperator.Equals, value);
        return this;
    }

    public SelectQuery<T, V> whereIsNull(String column) {
        where(column, SqlOperator.IsNull, null);
        return this;
    }

    public <X> SelectQuery<T, V> having(String key, SqlOperator operator, X value) {
        return this;
    }

    public SelectQuery<T, V> groupBy(String... columns) {
        Arrays.stream(columns)
              .map(column -> String.format("`%s`", column))
              .forEach(column -> this.groupBy.add(column));
        return this;
    }

    public SelectQuery<T, V> orderBy(String... columns) {
        Arrays.stream(columns)
              .forEach(column -> this.orderBy(column, OrderDirection.DESC));
        return this;
    }

    public SelectQuery<T, V> orderBy(String column, OrderDirection direction) {
        this.orderBy.put(String.format("`%s`", column), direction);
        return this;
    }

    public List<V> get() throws SQLException {
        PreparedStatement statement = DatabaseConnection.connection.prepareStatement(getSqlStatement());
        populateStatement(statement);
        ResultSet resultSet = statement.executeQuery();
        List<V> results = new LinkedList<>();
        while(resultSet.next()){
            Map<String, ColumnValue> attributes = new HashMap<>();
            for (String columnName : getColumnNames(resultSet.getMetaData()))
                attributes.put(columnName, new ColumnValue<>(resultSet.getObject(columnName)));
            model.getState().populateAttributes(attributes);
            results.add(model.getState());
        }
        return results;
    }

    public V first() throws SQLException {
        limit(1);
        PreparedStatement statement = DatabaseConnection.connection.prepareStatement(getSqlStatement());
        populateStatement(statement);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()) {
            Map<String, ColumnValue> attributes = new HashMap<>();
            for (String columnName : getColumnNames(resultSet.getMetaData())) {
                attributes.put(columnName, new ColumnValue<>(resultSet.getObject(columnName)));
            }
            model.getState().populateAttributes(attributes);
            return model.getState();
        }
        return null;
    }

    private List<String> getColumnNames(ResultSetMetaData data) throws SQLException {
        List<String> columnNames = new LinkedList<>();
        for (int i = 1; i <= data.getColumnCount(); i++) columnNames.add(data.getColumnName(i).toLowerCase());
        return columnNames;
    }

    public SelectQuery<T, V> limit(int limit) {
        if(limit < 0) throw new IllegalArgumentException("Limit must be a positive value");
        this.limit = limit;
        return this;
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
        if (this.limit > 0) {
            sql.append(String.format(" LIMIT %s", this.limit));
        }
        return sql.toString();
    }

    public SelectQuery<T, V> count(String column) {
        this.selects.add(String.format("COUNT(`%s`)", column));
        return this;
    }

    public SelectQuery<T, V> average(String column) {
        this.selects.add(String.format("AVG(`%s`)", column));
        return this;
    }

    public SelectQuery<T, V> sum(String column) {
        this.selects.add(String.format("SUM(`%s`)", column));
        return this;
    }

    public SelectQuery<T, V> max(String column) {
        this.selects.add(String.format("MAX(`%s`)", column));
        return this;
    }

    public SelectQuery<T, V> min(String column) {
        this.selects.add(String.format("MIN(`%s`)", column));
        return this;
    }

    public SelectQuery<T, V> join(BaseRecord model, String foreignKey, String localKey) {
        this.joins.add(new JoinStatement<>(model, foreignKey, localKey));
        return this;
    }

    public SelectQuery<T, V> join(BaseRecord model, String foreignKey, String localKey, JoinType joinType) {
        this.joins.add(new JoinStatement<>(model, foreignKey, localKey, joinType));
        return this;
    }

    public <X extends BaseRecord> SelectQuery<T, V> join(SelectQuery query, String alias, String foreignKey, String localKey, JoinType joinType) {
        this.joins.add(new JoinStatement<X>(query, alias, foreignKey, localKey, joinType));
        return this;
    }

    public <X extends BaseRecord> SelectQuery<T, V> join(SelectQuery query, String alias, String foreignKey, String localKey) {
        this.joins.add(new JoinStatement<X>(query, alias, foreignKey, localKey));
        return this;
    }
}
