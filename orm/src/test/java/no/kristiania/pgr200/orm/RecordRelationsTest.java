package no.kristiania.pgr200.orm;

import no.kristiania.pgr200.orm.Annotations.Relation;
import no.kristiania.pgr200.orm.Relations.*;
import no.kristiania.pgr200.orm.TestData.PhoneModel;
import no.kristiania.pgr200.orm.TestData.ProfilePictureModel;
import no.kristiania.pgr200.orm.TestData.User;
import no.kristiania.pgr200.orm.TestData.UserModel;
import org.assertj.core.api.MapAssert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RecordRelationsTest {
    @Test
    public void shouldConfigureRelationships() {
        UserModel userModel = new UserModel();

        Map<String, AbstractRelation> userRelations = userModel.getRelations();
        assertThat(userRelations).containsKey("profilePictures");
        assertThat(userRelations.get("profilePictures"))
                .hasFieldOrPropertyWithValue("relationRecord", ProfilePictureModel.class);
        assertThat(userRelations).containsKey("friends");
        assertThat(userRelations.get("friends"))
                .hasFieldOrPropertyWithValue("relationRecord", UserModel.class);
        assertThat(userRelations).containsKey("phone");
        assertThat(userRelations.get("phone"))
                .hasFieldOrPropertyWithValue("relationRecord", PhoneModel.class);

        ProfilePictureModel profilePictureModel = new ProfilePictureModel();

        Map<String, AbstractRelation> profilePictureRelations = profilePictureModel.getRelations();
        assertThat(profilePictureRelations).containsKey("user");
        assertThat(profilePictureRelations.get("user"))
                .hasFieldOrPropertyWithValue("relationRecord", UserModel.class);

        PhoneModel phoneModel = new PhoneModel();

        Map<String, AbstractRelation> phoneRelations = phoneModel.getRelations();
        assertThat(phoneRelations).containsKey("user");
        assertThat(phoneRelations.get("user"))
                .hasFieldOrPropertyWithValue("relationRecord", UserModel.class);
    }

    @Test
    public void shouldContainLocalAndForeignKeys() {
        UserModel userModel = new UserModel();

        HasOne<PhoneModel> hasOne = userModel.phone();

        assertThat(hasOne).hasFieldOrPropertyWithValue("foreignKey", "userId")
                          .hasFieldOrPropertyWithValue("localKey", "id");

        HasMany<ProfilePictureModel> hasMany = userModel.profilePictures();

        assertThat(hasMany).hasFieldOrPropertyWithValue("foreignKey", "userId")
                           .hasFieldOrPropertyWithValue("localKey", "id");

        BelongsTo<UserModel> belongsTo = new PhoneModel().user();

        assertThat(belongsTo).hasFieldOrPropertyWithValue("foreignKey", "id")
                             .hasFieldOrPropertyWithValue("localKey", "userId");
    }
    
    @Test
    public void shouldContainLocalAndForeignKeysOfBothTables() {
        HasManyThrough<UserModel> hasManyThrough = new UserModel().friends();

        assertThat(hasManyThrough)
                .hasFieldOrPropertyWithValue("pivotTable", "friends_table")
                .hasFieldOrPropertyWithValue("foreignKey", "userId")
                .hasFieldOrPropertyWithValue("localKey", "id")
                .hasFieldOrPropertyWithValue("relationForeignKey", "friendId")
                .hasFieldOrPropertyWithValue("relationLocalKey", "id");
    }

    @Test
    public void shouldHaveHelperMethodsToDefineRelations() {
        BaseRecord<User> user = new BaseRecord<User>(new User()) {
            @Override
            public String getTable() {
                return "users";
            }

            @Relation
            public HasOne<PhoneModel> phone() {
                return this.hasOne(PhoneModel.class);
            }

            @Relation
            public BelongsTo<PhoneModel> parent() {
                return this.belongsTo(PhoneModel.class);
            }

            @Relation
            public HasMany<ProfilePictureModel> profilePictures() {
                return this.hasMany(ProfilePictureModel.class);
            }
        };

        Map<String, AbstractRelation> relations = user.getRelations();

        assertThat(relations.get("phone"))
                .isInstanceOf(HasOne.class)
                .hasFieldOrPropertyWithValue("foreignKey", "user_id")
                .hasFieldOrPropertyWithValue("localKey", "id");

        assertThat(relations.get("parent"))
                .isInstanceOf(BelongsTo.class)
                .hasFieldOrPropertyWithValue("foreignKey", "phone_id")
                .hasFieldOrPropertyWithValue("localKey", "id");

        assertThat(relations.get("profilePictures"))
                .isInstanceOf(HasMany.class)
                .hasFieldOrPropertyWithValue("foreignKey", "user_id")
                .hasFieldOrPropertyWithValue("localKey", "id");
    }

    @Test
    public void shouldTestOverloadedMethods() {
        BaseRecord<User> mockRecord = mock(BaseRecord.class);
        when(mockRecord.getTable()).thenReturn("users");
        when(mockRecord.getPrimaryKey()).thenCallRealMethod();
        Class<? extends Class> baseRecordClass = BaseRecord.class.getClass();

        /* HasOne relationship */

        when(mockRecord.hasOne(isA(baseRecordClass))).thenCallRealMethod();

        mockRecord.hasOne(UserModel.class);
        verify(mockRecord).hasOne(eq(UserModel.class), eq("user_id"), eq("id"));

        when(mockRecord.hasOne(isA(baseRecordClass), anyString())).thenCallRealMethod();

        mockRecord.hasOne(UserModel.class, "userID");
        verify(mockRecord).hasOne(eq(UserModel.class), eq("userID"), eq("id"));

        /* HasMany relationship */

        when(mockRecord.hasMany(isA(baseRecordClass))).thenCallRealMethod();

        mockRecord.hasMany(UserModel.class);
        verify(mockRecord).hasMany(eq(UserModel.class), eq("user_id"), eq("id"));

        when(mockRecord.hasMany(isA(baseRecordClass), anyString())).thenCallRealMethod();

        mockRecord.hasMany(UserModel.class, "user");
        verify(mockRecord).hasMany(eq(UserModel.class), eq("user"), eq("id"));

        /* BelongsTo relationship */

        when(mockRecord.belongsTo(isA(baseRecordClass))).thenCallRealMethod();

        mockRecord.belongsTo(PhoneModel.class);
        verify(mockRecord).belongsTo(eq(PhoneModel.class), eq("phone_id"), eq("id"));

        when(mockRecord.belongsTo(isA(baseRecordClass), anyString())).thenCallRealMethod();

        mockRecord.belongsTo(PhoneModel.class, "mobile");
        verify(mockRecord).belongsTo(eq(PhoneModel.class), eq("mobile"), eq("id"));

        /* HasManyThrough relationship */
    }
}
