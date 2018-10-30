package no.kristiania.pgr200.orm.utils;

import com.hypertino.inflector.English;
import no.kristiania.pgr200.orm.BaseRecord;

public class RecordUtils {
    public static String GuessForeignKey(String tableName, String localKey) {
        return English.singular(tableName) + "_" + localKey;
    }

    public static <T extends BaseRecord> String GuessForeignKey(T modelClass, String localKey) {
        String tableName = modelClass.getTable();
        return GuessForeignKey(tableName, localKey);
    }

    public static <T extends BaseRecord> String GuessForeignKey(T modelClass) {
        String primaryKey = modelClass.getPrimaryKey();
        return GuessForeignKey(modelClass, primaryKey);
    }
}
