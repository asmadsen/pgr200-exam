package no.kristiania.pgr200.orm.Enums;

public enum SqlOperator {
    Addition("+"),
    Subtraction("-"),
    Multiplication("*"),
    Division("/"),
    Modulus("%"),
    Equals("="),
    NotEquals("!="),
    GreaterThan(">"),
    lessThan("<"),
    GreaterThanOrEqual(">="),
    lessThanOrEqual("<="),
    NotLessThan("!<"),
    NotGreaterThan("!>"),
    All("ALL"),
    And("AND"),
    Any("ANY"),
    Between("BETWEEN"),
    Exists("EXISTS"),
    In("IN"),
    Like("LIKE"),
    Not("NOT"),
    Or("OR"),
    Null("NULL"),
    NotNull("IS NOT NULL", false),
    IsNull("IS NULL", false),
    Unique("UNIQUE");

    private final String operator;
    private final boolean hasValue;
    SqlOperator(String operator) {
        this(operator, true);
    }

    SqlOperator(String operator, boolean hasValue) {
        this.operator = operator;
        this.hasValue = hasValue;
    }

    public String getOperator(){
        return this.operator;
    }

    public boolean hasValue() {
        return hasValue;
    }
}
