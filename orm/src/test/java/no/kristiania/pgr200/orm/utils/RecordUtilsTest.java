package no.kristiania.pgr200.orm.utils;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.test_data.PhoneModel;
import no.kristiania.pgr200.orm.test_data.UserModel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecordUtilsTest {
    @Test
    public void shouldGuessForeignKey() {
        assertThat(RecordUtils.GuessForeignKey(new UserModel(), "id"))
                .isEqualTo("user_id");

        assertThat(RecordUtils.GuessForeignKey(new PhoneModel(), "id"))
                .isEqualTo("phone_id");

        assertThat(RecordUtils.GuessForeignKey(new UserModel()))
                .isEqualTo("user_id");

        assertThat(RecordUtils.GuessForeignKey(new PhoneModel()))
                .isEqualTo("phone_id");

        BaseRecord postMock = mock(BaseRecord.class);

        when(postMock.getTable()).thenReturn("posts");
        when(postMock.getPrimaryKey()).thenReturn("slug");

        assertThat(RecordUtils.GuessForeignKey(postMock))
                .isEqualTo("post_slug");
    }
}
