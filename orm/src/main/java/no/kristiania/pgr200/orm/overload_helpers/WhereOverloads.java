package no.kristiania.pgr200.orm.overload_helpers;

import no.kristiania.pgr200.orm.enums.SqlOperator;

public interface WhereOverloads<T> {
    <V> T where(String column, SqlOperator operator, V value, boolean useAnd);

    default <V> T where(String column, SqlOperator operator, V value) {
        return this.where(column, operator, value, true);
    }

    default <V> T whereNot(String column, V value) {
        return this.where(column, SqlOperator.Not, value);
    }

    default T whereNotNull(String column) {
        return this.where(column, SqlOperator.NotNull, null);
    }

    default <V> T whereEquals(String column, V value) {
        return this.where(column, SqlOperator.Equals, value);
    }

    default T whereIsNull(String column) {
        return this.where(column, SqlOperator.IsNull, null);
    }
}
