package no.kristiania.pgr200.orm;


public class JoinStatement<T extends BaseModel> {
    private T model;
    private Query query;
    private String foreignKey, localKey;
    private JoinType type;

    public JoinStatement(T model, String foreignKey, String localKey, JoinType joinType) {
        this.model = model;
        this.foreignKey = foreignKey;
        this.localKey = localKey;
        this.type = joinType;
    }

    public JoinStatement(T model, String foreignKey, String localKey) {
        this(model, foreignKey, localKey, JoinType.LeftJoin);
    }

    public String getSqlStatement() {
        return String.format(
                "%s `%s` ON `%s`.`%s` = `?`.`%s`",
                this.type.getSql(),
                this.model.getTable(),
                this.model.getTable(),
                this.foreignKey,
                this.localKey
        );
    }

}
