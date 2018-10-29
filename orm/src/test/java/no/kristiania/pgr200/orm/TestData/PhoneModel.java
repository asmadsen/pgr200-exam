package no.kristiania.pgr200.orm.TestData;

import no.kristiania.pgr200.orm.Annotations.Relation;
import no.kristiania.pgr200.orm.BaseRecord;
import no.kristiania.pgr200.orm.Relations.BelongsTo;

import java.util.UUID;

public class PhoneModel extends BaseRecord<PhoneModel, Phone> {

    public PhoneModel() {
        super(new Phone());
    }

    public PhoneModel(UUID id, UUID userId, String phoneNumber) {
        super(new Phone(id, userId, phoneNumber));
    }

    public PhoneModel(UUID userId, String phoneNumber) {
        this(null, userId, phoneNumber);
    }

    public static PhoneModel Create(UUID id, UUID userId, String phoneNumber) {
        PhoneModel phoneModel = new PhoneModel(id, userId, phoneNumber);
        phoneModel.save();
        return phoneModel;
    }

    @Override
    public String getTable() {
        return "phone";
    }

    @Relation
    public BelongsTo<UserModel, User, PhoneModel> user() {
        return this.belongsTo(new UserModel(), "user_id", "id");
    }
}
