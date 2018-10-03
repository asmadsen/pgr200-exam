package no.kristiania.pgr200.server.models;

import no.kristiania.pgr200.common.Http.HttpUtils;
import no.kristiania.pgr200.server.db.DatabaseHandling;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Records<T> {
    StringBuilder statement;

    /**
     * Generic way to find all rows in a table.
     * @param table Table name
     * @param columns List of columns to query
     * @return A list of models
     */
    public List<T> query(String table, String... columns) {
        List<T> modelList = new ArrayList<>();
        statement = buildSelectStatement(table, columns);
        try(PreparedStatement preparedStatement = DatabaseHandling.getConnection().prepareStatement(statement.toString())) {
            ResultSet talkResult = DatabaseHandling.selectStatement(preparedStatement);
            while (talkResult.next()) {
                for (String column : columns) {
                    this.getClass().getDeclaredMethod("set" + HttpUtils.capitalizeHeaderKey(column), String.class)
                            .invoke(this, talkResult.getString(column));
                }
                modelList.add((T) this);
            }
        } catch (SQLException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return modelList;
    }

    // TODO: Add possibility to use WHERE etc
    public StringBuilder buildSelectStatement(String table, String... columns){
        statement = new StringBuilder()
                .append("SELECT ").append(String.join(", ", columns))
                .append(" FROM ").append(table).append(";");
        return statement;
    }
}
