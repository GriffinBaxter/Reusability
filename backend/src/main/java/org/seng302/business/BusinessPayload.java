package org.seng302.business;

import org.seng302.Address.Address;
import org.seng302.user.User;
import org.seng302.user.UserPayload;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BusinessPayload {
    private int id;
    private List<UserPayload> administrators;
    private String name;
    private String description;
    private Address address;
    private String businessType;
    private String created;

    public BusinessPayload(int id,
                           List<User> administrators,
                           String name,
                           String description,
                           String address,
                           BusinessType businessType,
                           LocalDateTime created) {
        this.id = id;
        this.administrators = UserPayload.toUserPayload(administrators);
        this.name = name;
        this.description = description;
        this.address = Address.toAddress(address);
        this.businessType = businessType.toString();
        this.created = created.toString();
    }

    public int getId() {
        return id;
    }

    public List<UserPayload> getAdministrators() {
        return administrators;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Address getAddress() {
        return address;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getCreated() {
        return created;
    }
}
