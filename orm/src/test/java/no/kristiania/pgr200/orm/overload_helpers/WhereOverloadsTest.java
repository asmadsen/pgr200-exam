package no.kristiania.pgr200.orm.overload_helpers;

import no.kristiania.pgr200.orm.enums.SqlOperator;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WhereOverloadsTest {
    @Test
    public void shouldCallMainMethod() {
        WhereOverloads whereOverloads = mock(WhereOverloads.class);

        when(whereOverloads.where(anyString(), any(SqlOperator.class), any())).thenCallRealMethod();
        when(whereOverloads.whereNot(anyString(), any())).thenCallRealMethod();
        when(whereOverloads.whereNotNull(anyString())).thenCallRealMethod();
        when(whereOverloads.whereEquals(anyString(), any())).thenCallRealMethod();
        when(whereOverloads.whereIsNull(anyString())).thenCallRealMethod();

        whereOverloads.where("column1", SqlOperator.GreaterThan, 1);
        whereOverloads.whereNot("column2", 5);
        whereOverloads.whereNotNull("column3");
        whereOverloads.whereEquals("column4", "blue");
        whereOverloads.whereIsNull("column5");

        InOrder inOrder = Mockito.inOrder(whereOverloads);
        inOrder.verify(whereOverloads).where("column1", SqlOperator.GreaterThan, 1, true);
        inOrder.verify(whereOverloads).where("column2", SqlOperator.Not, 5, true);
        inOrder.verify(whereOverloads).where("column3", SqlOperator.NotNull, null, true);
        inOrder.verify(whereOverloads).where("column4", SqlOperator.Equals, "blue", true);
        inOrder.verify(whereOverloads).where("column5", SqlOperator.IsNull, null, true);
    }
}
