package org.seng302.view.incoming;

import org.seng302.model.Address;
import org.seng302.model.enums.BusinessType;
import org.seng302.view.outgoing.AddressPayload;

/**
 * Describes the payload returned by the modification request for a business.
 */
public class BusinessModifyPayload {

    private Integer primaryAdminId;
    private String name;
    private String description;
    private AddressPayload address;
    private String businessType;

    public Integer getPrimaryAdminId() {
        return primaryAdminId;
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

    public BusinessType getBusinessType() {
        return BusinessType.valueOf(businessType.toUpperCase());
    }
}
