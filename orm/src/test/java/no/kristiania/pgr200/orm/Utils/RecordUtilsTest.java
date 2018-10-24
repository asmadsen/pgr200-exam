package no.kristiania.pgr200.orm.Utils;

import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.TestData.PhoneModel;
import no.kristiania.pgr200.orm.TestData.ProfilePictureModel;
import no.kristiania.pgr200.orm.TestData.UserModel;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RecordUtilsTest {
    @Test
    public void shouldGuessForeignKey() {
        assertThat(RecordUtils.GuessForeignKey(new UserModel(), "id"))
                .isEqualTo("user_id");

        assertThat(RecordUtils.GuessForeignKey(new PhoneModel(), "id"))
                .isEqualTo("phone_id");

        assertThat(RecordUtils.GuessForeignKey(new ProfilePictureModel(), "id"))
                .isEqualTo("profile_picture_id");

        assertThat(RecordUtils.GuessForeignKey(new UserModel()))
                .isEqualTo("user_id");

        assertThat(RecordUtils.GuessForeignKey(new PhoneModel()))
                .isEqualTo("phone_id");

        assertThat(RecordUtils.GuessForeignKey(new ProfilePictureModel()))
                .isEqualTo("profile_picture_id");

        assertThat(RecordUtils.GuessForeignKey(new BaseRecord() {
            @Override
            public String getTable() {
                return "posts";
            }

            @Override
            public String getPrimaryKey() {
                return "slug";
            }
        }))
                .isEqualTo("post_slug");
    }
}
