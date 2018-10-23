package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Enums.Statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.StringJoiner;

public class InsertQuery<T extends IBaseModel<T>> extends BaseQuery<InsertQuery<T>> {
    private T model;

    public InsertQuery(String table) {
        super(table);
    }

    String getSqlStatement() {
        StringBuilder sql = new StringBuilder();
        StringJoiner values = new StringJoiner(", ");
        model.getAttributes().keySet().forEach(e -> values.add("?"));
        sql.append(String.format("%s `%s` (`%s`) %s (%s);",
                Statement.INSERT.getStatement(), getTable(), String.join("`, `", model.getAttributes().keySet()),
                Statement.VALUES, values.toString()));
        return sql.toString();
    }

    public InsertQuery<T> insert(BaseRecord model) {
        this.model = (T) model.getState();
        return this;
    }

    @Override
    public void populateStatement(PreparedStatement statement) throws SQLException {
        for (Map.Entry<String, ColumnValue> value : model.getAttributes().entrySet()) {
            statement.setObject(counter++, value.getValue().getValue());
        }
    }
}
