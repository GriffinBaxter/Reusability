package org.seng302.view.incoming;

import org.seng302.model.enums.BusinessType;
import org.seng302.view.outgoing.AddressPayload;

/**
 * Describes the payload returned by the modification request for a business.
 */
public class BusinessModifyPayload {

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

    public String toString() {
        String tmp =  "{" +
                "\"primaryAdministratorId\":" + primaryAdministratorId + "," +
                "\"name\":\"" + name + "\"," +
                "\"description\":\"" + description + "\",";
                if (address != null) {
                    tmp += "\"address\":" + address.toString() + ",";
                }
                tmp += "\"businessType\":\"" + businessType + "\"," +
                "\"currencySymbol\":\"" + currencySymbol + "\"," +
                "\"currencyCode\":\"" + currencyCode + "\"" +
                "}";
        return tmp;
    }

    /**
     * check if a string is a business type, if so make the string to businessType object, if not, return null
     * @param string a string about business type
     * @return when string is business type return BusinessType object, if not return null
     */
    private BusinessType businessTypeTranslate(String string){
        BusinessType translatedType = null;
        if (string == null) {
            return null;
        }else {
            string = string.toUpperCase();
        }

        if (string.equals("ACCOMMODATION AND FOOD SERVICES")){
            translatedType = BusinessType.ACCOMMODATION_AND_FOOD_SERVICES;
        } else if (string.equals("RETAIL TRADE")){
            translatedType = BusinessType.RETAIL_TRADE;
        } else if (string.equals("CHARITABLE ORGANISATION")){
            translatedType = BusinessType.CHARITABLE_ORGANISATION;
        } else if (string.equals("NON PROFIT ORGANISATION")){
            translatedType = BusinessType.NON_PROFIT_ORGANISATION;
        }
        return translatedType;
    }
}
