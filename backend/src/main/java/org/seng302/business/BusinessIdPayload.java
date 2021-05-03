package org.seng302.business;

/**
 * Payload for the Business ID.
 * Returned when new business is created.
 */
public class BusinessIdPayload {
    private int businessId;

    public BusinessIdPayload(int businessId) {
        this.businessId = businessId;
    }

    public int getBusinessId() {
        return businessId;
    }

    public BusinessIdPayload() {
    }
}

