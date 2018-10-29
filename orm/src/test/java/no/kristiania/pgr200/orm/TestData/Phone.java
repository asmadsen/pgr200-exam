package no.kristiania.pgr200.orm.TestData;

import java.util.Objects;
import java.util.UUID;

public class Phone extends BaseModel<Phone> {
    protected UUID id;
    public UUID user_id;
    public String phone_number;

    public Phone() {
    }

    public Phone(UUID id, UUID userId, String phoneNumber) {
        this.id = id;
        this.user_id = userId;
        this.phone_number = phoneNumber;
    }

    public Phone(UUID userId, String phoneNumber) {
        this(null, userId, phoneNumber);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return user_id;
    }

    public void setUserId(UUID userId) {
        this.user_id = userId;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Phone) {
            return this.getId().equals(((Phone) other).getId()) &&
                    this.getUserId().equals(((Phone) other).getUserId()) &&
                    this.getPhoneNumber().equals(((Phone) other).getPhoneNumber());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, phone_number);
    }
}
