package no.kristiania.pgr200.orm.enums;

public enum JoinType {
    LeftJoin("LEFT JOIN"),
    FullOuterJoin("FULL OUTER JOIN"),
    InnerJoin("INNER JOIN"),
    RightJoin("RIGHT JOIN");

    private final String sql;

    JoinType(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }
}
