package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.enums.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class InsertQuery<T extends IBaseModel<T>> {
    private final String table;
    private T model;
    private Map<String, ColumnValue> attributes;
    private static Logger logger = LoggerFactory.getLogger(InsertQuery.class);

    public InsertQuery(String table) {
        this.table = table;
    }

    public String getTable() {
        return table;
    }

    String getSqlStatement() {
        StringBuilder sql = new StringBuilder();
        StringJoiner values = new StringJoiner(", ");
        attributes = model.getAttributes();
        attributes = attributes.entrySet().stream().filter(e -> e.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        attributes.keySet().forEach(e -> values.add("?"));
        sql.append(String.format("%s %s (%s) %s (%s)",
                                 Statement.INSERT.getStatement(),
                                 Orm.QuoteString(getTable()),
                                 String.join(", ", attributes
                                                        .keySet()
                                                        .stream()
                                                        .map(Orm::QuoteString)
                                                        .collect(Collectors.toList())),
                                 Statement.VALUES, values.toString()));
        return sql.toString();
    }

    public InsertQuery<T> insert(BaseRecord model) {
        this.model = (T) model.getState();
        return this;
    }

    public void populateStatement(PreparedStatement statement) throws SQLException {
        int counter = 1;
        for (Map.Entry<String, ColumnValue> value : this.attributes.entrySet()) {
            statement.setObject(counter++, value.getValue().getValue());
        }
    }

    public int get() throws SQLException {
        PreparedStatement statement = Orm.connection.prepareStatement(getSqlStatement());
        populateStatement(statement);
        logger.info(statement.toString());
        return statement.executeUpdate();
    }
}
