/**
 * Summary. This file contains the definition for the BusinessPayload.
 *
 * Description. This file contains the defintion for the BusinessPayload.
 *
 * @link   team-400/src/main/java/org/seng302/business/BusinessPayload
 * @file   This file contains the definition for BusinessPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.outgoing;

import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 * Business Payload
 *
 * Business payload to send to the API.
 *
 * @link   team-400/src/main/java/org/seng302/business/BusinessPayload.java
 * @file   BusinessPayload.java.
 * @author team-400.
 * @since  5.5.2021
 */
public class BusinessPayload {
    private Integer id;
    private List<UserPayload> administrators;
    private Integer primaryAdministratorId;
    private String name;
    private String description;
    private AddressPayload address;
    private String businessType;
    private String created;

    /**
     * translate a list of Business to a list of BusinessPayload
     * @param businesses a list of businesses
     * @return a list of BusinessPayload
     */
    public static List<BusinessPayload> toBusinessPayload (List<Business> businesses) throws Exception {
        List<BusinessPayload> businessPayloads = new ArrayList<>();
        BusinessPayload businessPayload;
        for (Business business: businesses){
            Address address = business.getAddress();
            AddressPayload addressPayload = new AddressPayload(
                    address.getStreetNumber(),
                    address.getStreetName(),
                    address.getCity(),
                    address.getRegion(),
                    address.getCountry(),
                    address.getPostcode(),
                    address.getSuburb()
            );
            businessPayload = new BusinessPayload(
                    business.getId(),
                    business.getAdministrators(),
                    business.getPrimaryAdministratorId(),
                    business.getName(),
                    business.getDescription(),
                    addressPayload,
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
                           AddressPayload address,
                           BusinessType businessType,
                           LocalDateTime created
                           ) throws Exception {
        this.id = id;
        this.administrators = UserPayload.convertToPayloadWithoutBusiness(administrators);
        //      TODO This might get changed in the future due to the recursive nature of the API seems wrong.
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

    public AddressPayload getAddress() {
        return address;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getCreated() {
        return created;
    }
}
