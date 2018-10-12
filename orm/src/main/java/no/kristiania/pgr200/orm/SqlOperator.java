package no.kristiania.pgr200.orm;

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
    Or("Or"),
    Null("NULL"),
    IsNull("IS NULL"),
    Unique("UNIQUE");

    private final String operator;
    SqlOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator(){
        return this.operator;
    }

}
