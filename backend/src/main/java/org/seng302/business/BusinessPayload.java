package org.seng302.business;

import org.seng302.address.Address;
import org.seng302.user.User;
import org.seng302.user.UserPayload;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BusinessPayload {
    private Integer primaryAdministratorId;
    private Integer id;
    private List<UserPayload> administrators;
    private String name;
    private String description;
    private Address address;
    private String businessType;
    private String created;

    /**
     * translate a list of Business to a list of BusinessPayload
     * @param businesses a list of businesses
     * @return a list of BusinessPayload
     */
    public static List<BusinessPayload> toBusinessPayload (List<Business> businesses){
        List<BusinessPayload> businessPayloads = new ArrayList<>();
        BusinessPayload businessPayload;
        for (Business business: businesses){
            businessPayload = new BusinessPayload(
                    business.getId(),
                    business.getAdministrators(),
                    business.getPrimaryAdministratorId(),
                    business.getName(),
                    business.getDescription(),
                    business.getAddress(),
                    business.getBusinessType(),
                    business.getCreated()
            );
            businessPayloads.add(businessPayload);
        }
        return businessPayloads;
    }

    public BusinessPayload(int id,
                           List<User> administrators,
                           Integer primaryAdministratorId,
                           String name,
                           String description,
                           Address address,
                           BusinessType businessType,
                           LocalDateTime created
                           ) {
        this.id = id;
        this.administrators = UserPayload.toUserPayload(administrators);
        if (this.administrators.isEmpty()){
            this.administrators.add(null);
        }
        this.primaryAdministratorId = primaryAdministratorId;
        this.name = name;
        this.description = description;
        this.address = address;
        this.businessType = businessType.toString();
        this.created = created.toString();
    }

    public int getId() {
        return id;
    }

    public List<UserPayload> getAdministrators() {
        return administrators;
    }

    public Integer getPrimaryAdministratorId() {
        return primaryAdministratorId;
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
