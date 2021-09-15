/**
 * Summary. This file contains the definition for the BusinessRegistrationPayload.
 *
 * Description. This file contains the defintion for the BusinessRegistrationPayload.
 *
 * @link   team-400/src/main/java/org/seng302/business/BusinessRegistrationPayload
 * @file   This file contains the definition for BusinessRegistrationPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.incoming;

import org.seng302.model.enums.BusinessType;
import org.seng302.view.outgoing.AddressPayload;

public class BusinessRegistrationPayload {

    private Integer primaryAdministratorId;
    private String name;
    private String description;
    private AddressPayload address;
    private String businessType;
    private String currencySymbol;
    private String currencyCode;

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

    public BusinessType getBusinessType() {
        return businessTypeTranslate(businessType);
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * check if a string is a business type, if so make the string to businessType object, if not, return null
     * @param string a string about business type
     * @return when string is business type return BusinessType object, if not return null
     */
    private BusinessType businessTypeTranslate(String string){
        BusinessType businessType = null;
        if (string.toUpperCase().equals("ACCOMMODATION AND FOOD SERVICES")){
            businessType = BusinessType.ACCOMMODATION_AND_FOOD_SERVICES;
        } else if (string.toUpperCase().equals("RETAIL TRADE")){
            businessType = BusinessType.RETAIL_TRADE;
        } else if (string.toUpperCase().equals("CHARITABLE ORGANISATION")){
            businessType = BusinessType.CHARITABLE_ORGANISATION;
        } else if (string.toUpperCase().equals("NON PROFIT ORGANISATION")){
            businessType = BusinessType.NON_PROFIT_ORGANISATION;
        }
        return businessType;
    }
}
