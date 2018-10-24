package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.Annotations.Relation;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.Relations.BelongsTo;

import java.util.UUID;

public class PhoneModel extends BaseRecord<Phone> {

    public PhoneModel() {
        this.setState(new Phone());
    }

    public PhoneModel(UUID id, UUID userId, String phoneNumber, boolean verified) {
        this.setState(new Phone(id, userId, phoneNumber, verified));
    }

    public PhoneModel(UUID userId, String phoneNumber, boolean verified) {
        this(null, userId, phoneNumber, verified);
    }

    @Override
    public String getTable() {
        return "phone";
    }

    @Relation
    public BelongsTo<UserModel> user() {
        return this.belongsTo(UserModel.class, "id", "userId");
    }
}
