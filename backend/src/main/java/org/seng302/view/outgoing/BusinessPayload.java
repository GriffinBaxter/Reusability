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


import org.seng302.model.Business;
import org.seng302.model.BusinessImage;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Business payload
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
    private String currencySymbol;
    private String currencyCode;
    private List<BusinessImage> businessImages;

    /**
     * translate a list of Business to a list of BusinessPayload
     * @param businesses a list of businesses
     * @return a list of BusinessPayload
     */
    public static List<BusinessPayload> toBusinessPayload (List<Business> businesses) throws Exception {
        List<BusinessPayload> businessPayloads = new ArrayList<>();
        for (Business business: businesses){
            businessPayloads.add(business.toBusinessPayload());
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
                           LocalDateTime created,
                           String currencySymbol,
                           String currencyCode,
                           List<BusinessImage> businessImages
                           ) throws Exception {
        this.id = id;
        this.administrators = UserPayload.convertToPayloadWithoutBusiness(administrators);
        if (this.administrators.isEmpty()){
            this.administrators.add(null);
        }
        this.primaryAdministratorId = primaryAdministratorId;
        this.name = name;
        this.description = description;
        this.address = address;
        this.businessType = businessType.toString();
        this.created = created.toString();
        this.currencySymbol = currencySymbol;
        this.currencyCode = currencyCode;
        this.businessImages = businessImages;
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

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public List<BusinessImage> getBusinessImages() {
        return businessImages;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id +
               ",\"administrators\":" + administrators +
                ",\"primaryAdministratorId\":" + primaryAdministratorId +
                ",\"name\":\"" + name + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"address\":" + address +
                ",\"businessType\":\"" + businessType + "\"," +
                "\"created\":\"" + created + "\"," +
                "\"currencySymbol\":\"" + currencySymbol + "\"," +
                "\"currencyCode\":\"" + currencyCode + "\"," +
                "\"businessImages\":" + businessImages +
                "}";
    }
}
