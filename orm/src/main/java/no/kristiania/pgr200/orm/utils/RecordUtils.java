package no.kristiania.pgr200.orm.utils;

import com.hypertino.inflector.English;
import no.kristiania.pgr200.orm.BaseRecord;

public class RecordUtils {
    public static <T extends BaseRecord> String GuessForeignKey(T modelClass, String localKey) {
        String tableName = modelClass.getTable();
        return English.singular(tableName) + "_" + localKey;
    }

    public static <T extends BaseRecord> String GuessForeignKey(T modelClass) {
        String primaryKey = modelClass.getPrimaryKey();
        return GuessForeignKey(modelClass, primaryKey);
    }
}
