package no.kristiania.pgr200.orm;

import jdk.nashorn.internal.ir.ReturnNode;
import no.kristiania.pgr200.orm.Annotations.Relation;
import no.kristiania.pgr200.orm.Enums.SqlOperator;
import no.kristiania.pgr200.orm.Enums.Statement;
import no.kristiania.pgr200.orm.Relations.AbstractRelation;
import no.kristiania.pgr200.orm.Relations.BelongsTo;
import no.kristiania.pgr200.orm.Relations.HasMany;
import no.kristiania.pgr200.orm.Relations.HasOne;
import no.kristiania.pgr200.orm.Utils.RecordUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public abstract class BaseRecord<T extends IBaseModel<T>> {
    protected IBaseModel state;
    protected T model;
    private BaseRecord dbState;

    public abstract String getTable();

    public String getPrimaryKey() {
        return "id";
    }

    protected boolean save() {
        try {
            String statement;
            if (!exists()) {
                statement = insertStatement();
                if (executeStatement(statement, true) > 0) return true;
            } else {
                statement = updateStatement();
                if (executeStatement(statement, false) > 0) {
                    return true;
                } else {
                    statement = insertStatement();
                    if (executeStatement(statement, true) > 0) return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean create() {
        return false;
    }

    public T getModel() {
        return model;
    }

    // TODO: Refactor to be more readable
    protected String insertStatement() {
        StringJoiner values = new StringJoiner(", ");
        model.getAttributes().keySet().forEach(e -> values.add("?"));
        return String.format("%s `%s` (`%s`) %s (%s);",
                             Statement.INSERT.getStatement(),
                             getTable(),
                             String.join("`, `", model.getAttributes().keySet()),
                             Statement.VALUES,
                             values.toString());
    }

    // TODO: Refactor to be more readable
    protected String updateStatement() {
        StringJoiner columns = new StringJoiner(", ");
        model.getAttributes().entrySet().stream()
             .filter(set -> !set.getKey().equals("id"))
             .forEach(e -> columns.add("`" + e.getKey() + "` " + SqlOperator.Equals.getOperator() + " ?"));
        LinkedList<ConditionalStatement> list = new LinkedList<>();
        list.add(new ConditionalStatement<>("id", SqlOperator.Equals, model.getAttributes().get("id").getValue()));
        return String.format("%s `%s` %s %s %s;",
                             Statement.UPDATE.getStatement(), getTable(),
                             Statement.SET.getStatement(), columns.toString(),
                             ConditionalStatement.buildStatements(list));
    }

    private int executeStatement(String statement, boolean insert) throws SQLException {
        PreparedStatement preparedStatement = DatabaseConnection.connection.prepareStatement(statement);
        int count = 1;
        for (Map.Entry<String, ColumnValue> value : model.getAttributes().entrySet()) {
            if (!insert && value.getKey().equals("id")) continue;
            if (insert && value.getKey().equals("id")) {
                preparedStatement.setObject(count++, UUID.randomUUID());
                continue;
            }
            preparedStatement.setObject(count++, value.getValue().getValue());
        }
        if (!insert) preparedStatement.setObject(count, model.getAttributes().get("id").getValue());
        return preparedStatement.executeUpdate();
    }

    public boolean exists() {
        return false;
    }

    public Map<String, AbstractRelation> getRelations() {
        Map<String, AbstractRelation> relations = new HashMap<>();
        for (Method declaredMethod : getClass().getDeclaredMethods()) {
            if (!declaredMethod.isAnnotationPresent(Relation.class)) continue;
            if (!declaredMethod.getReturnType().getSuperclass().equals(AbstractRelation.class)) continue;
            try {
                AbstractRelation<? extends BaseRecord> relation = (AbstractRelation<? extends BaseRecord>) declaredMethod.invoke(this);
                relations.put(
                        declaredMethod.getName(),
                        relation
                );
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return relations;
    }

    protected <V extends BaseRecord> HasOne<V> hasOne(Class<V> modelClass) {
        String primaryKey = this.getPrimaryKey();
        String foreignKey = RecordUtils.GuessForeignKey(this, primaryKey);
        return this.hasOne(modelClass, foreignKey, primaryKey);
    }

    protected <V extends BaseRecord> HasOne<V> hasOne(Class<V> modelClass, String foreignKey) {
        String primaryKey = this.getPrimaryKey();
        return this.hasOne(modelClass, foreignKey, primaryKey);
    }

    protected <V extends BaseRecord> HasOne<V> hasOne(Class<V> modelClass, String foreignKey, String localKey) {
        return new HasOne<>(modelClass, foreignKey, localKey);
    }

    protected <V extends BaseRecord> BelongsTo<V> belongsTo(Class<V> modelClass) {
        try {
            V model = modelClass.newInstance();
            String primaryKey = model.getPrimaryKey();
            String foreignKey = RecordUtils.GuessForeignKey(model, primaryKey);
            return this.belongsTo(modelClass, foreignKey, primaryKey);
        } catch (InstantiationException | IllegalAccessException ignored) {}
        return null;
    }

    protected <V extends BaseRecord> BelongsTo<V> belongsTo(Class<V> modelClass, String foreignKey) {
        try {
            V model = modelClass.newInstance();
            String primaryKey = model.getPrimaryKey();
            return this.belongsTo(modelClass, foreignKey, primaryKey);
        } catch (InstantiationException | IllegalAccessException ignored) {}
        return null;
    }

    protected <V extends BaseRecord> BelongsTo<V> belongsTo(Class<V> modelClass, String foreignKey, String localKey) {
        return new BelongsTo<>(modelClass, foreignKey, localKey);
    }

    protected <V extends BaseRecord> HasMany<V> hasMany(Class<V> modelClass) {
        String primaryKey = this.getPrimaryKey();
        String foreignKey = RecordUtils.GuessForeignKey(this, primaryKey);
        return this.hasMany(modelClass, foreignKey, primaryKey);
    }

    protected <V extends BaseRecord> HasMany<V> hasMany(Class<V> modelClass, String foreignKey) {
        String primaryKey = this.getPrimaryKey();
        return this.hasMany(modelClass, foreignKey, primaryKey);
    }

    protected <V extends BaseRecord> HasMany<V> hasMany(Class<V> modelClass, String foreignKey, String localKey) {
        return new HasMany<>(modelClass, foreignKey, localKey);
    }
}
