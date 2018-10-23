package no.kristiania.pgr200.orm;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ColumnValueTest {

    @Test
    public void shouldHoldDifferentValues(){
        ColumnValue columnValue = new ColumnValue<>("String");
        assertThat(columnValue.getType()).isEqualTo(String.class);
        assertThat(columnValue.getValue()).isEqualTo("String");

        columnValue = new ColumnValue<>(123);
        assertThat(columnValue.getType()).isEqualTo(Integer.class);
        assertThat(columnValue.getValue()).isEqualTo(123);
    }

    @Test
    public void shouldCompareValues(){
        ColumnValue columnValue1 = new ColumnValue<>("String1");
        ColumnValue columnValue2 = new ColumnValue<>("String2");

        assertNotEquals(columnValue1, columnValue2);

        columnValue2 = new ColumnValue<>("String1");

        assertEquals(columnValue1, columnValue2);
        assertNotEquals(columnValue1, "String1");
    }
}
