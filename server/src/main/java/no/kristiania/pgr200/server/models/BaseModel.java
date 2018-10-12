package no.kristiania.pgr200.server.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import no.kristiania.pgr200.server.annotations.Record;
import no.kristiania.pgr200.server.db.DatabaseHandling;
import no.kristiania.pgr200.server.query.Query;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface BaseModel<T> {

    String TABLE = null;

    default boolean save(){
        return false;
    }

    default <K, V> boolean update(Map<K, V> attributes){
        return false;
    }

    default <K, V> BaseModel<T> fill(Map<K, V> attributes){
        return false;
    }

    default boolean isDirty(){
        return false;
    }

    default boolean delete(){
        return false;
    }

    static <T> List<T> all(Class<T> tClass) throws SQLException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        List<T> records = new ArrayList<>();
        BaseModel record = (BaseModel) tClass.newInstance();
        Query query = new Query(record.TABLE);
        ResultSet results = query.execute();
        while (results.next()) {
            Arrays.stream(tClass.getDeclaredMethods())
                    .filter(a -> a.getAnnotation(Record.class).type().equals("SET")).forEach(m -> {
                try {
                    m.invoke(record, results.getString(m.getAnnotation(Record.class).column()));
                } catch (IllegalAccessException | InvocationTargetException | SQLException e) {
                    e.printStackTrace();
                }
            });
            records.add((T) record);
        }
        return records;
    }

    static <T> T findBy(Class<T> tClass, int id) throws IllegalAccessException, InstantiationException, SQLException {
        BaseModel record = (BaseModel) tClass.newInstance();
        ResultSet resultSet = new Query<TalkModel>(record.TABLE).findBy(TalkModel.class, id).execute();
        if (resultSet.next()){

        }
    }

    // TODO: USE SAVE!
    @Deprecated
    default JsonElement create() throws SQLException {
//        String statement = "INSERT INTO talks(title, description) VALUES (?, ?)";
//        PreparedStatement preparedStatement = DatabaseHandling.getConnection().prepareStatement(statement);
//        String[] columns = getColumns();
//        Query query = new Query<T>(this.TABLE).insert((T) this, columns);
//        preparedStatement.setString(1, getTitle());
//        preparedStatement.setString(2, this.getDescription());
//        DatabaseHandling.executeStatement(preparedStatement);
//        ResultSet newTalk = DatabaseHandling.selectStatement(
//                DatabaseHandling.getConnection().prepareStatement("SELECT id FROM talks ORDER BY id DESC LIMIT 1"));
//        if(newTalk.next()){
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("id", newTalk.getString("id"));
//            jsonObject.addProperty("url", "http://localhost:8080/api/talks/" + newTalk.getString("id"));
//            return jsonObject;
//        }
    }

    default String[] getColumns(){
        return Arrays.stream(this.getClass().getAnnotationsByType(Record.class))
                .filter(a -> a.type().equals("SET")).map(Record::column).toArray(String[]::new);
    }
}
