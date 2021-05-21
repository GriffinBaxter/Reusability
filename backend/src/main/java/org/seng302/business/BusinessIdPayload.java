/**
 * Summary. This file contains the definition for the BusinessIdPayload.
 *
 * Description. This file contains the defintion for the BusinessIdPayload.
 *
 * @link   team-400/src/main/java/org/seng302/business/BusinessIdPayload
 * @file   This file contains the definition for BusinessIdPayload.
 * @author team-400.
 * @since  5.5.2021
 */
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

