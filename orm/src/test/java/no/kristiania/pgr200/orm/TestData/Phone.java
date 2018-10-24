package no.kristiania.pgr200.orm.TestData;

import java.util.Objects;
import java.util.UUID;

public class Phone extends BaseModel<Phone> {
    protected UUID id;
    public UUID userId;
    public String phoneNumber;
    public boolean verified;

    public Phone() {
    }

    public Phone(UUID id, UUID userId, String phoneNumber, boolean verified) {
        this.id = id;
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.verified = verified;
    }

    public Phone(UUID userId, String phoneNumber, boolean verified) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.verified = verified;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Phone) {
            return this.getId().equals(((Phone) other).getId()) &&
                    this.getUserId().equals(((Phone) other).getUserId()) &&
                    this.getPhoneNumber().equals(((Phone) other).getPhoneNumber()) &&
            this.isVerified() == ((Phone) other).isVerified();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, phoneNumber, verified);
    }
}
