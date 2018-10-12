package java.no.kristiania.pgr200.orm;

import no.kristiania.pgr200.server.models.BaseModel;

public class JoinStatement {
    private Class<BaseModel> table;
    private Query query;
    private String foreignKey, localKey;
    private JoinType type;
}
