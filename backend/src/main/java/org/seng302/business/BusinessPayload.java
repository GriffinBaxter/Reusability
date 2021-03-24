package org.seng302.business;

public class BusinessPayload {
    private String name;
    private String description;
    private String address;
    private String businessType;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public BusinessType getBusinessType() {
        return businessTypeTranslate(businessType);
    }

    private BusinessType businessTypeTranslate(String string){
        BusinessType businessType;
        if (string.toUpperCase().equals("ACCOMMODATION AND FOOD SERVICES")){
            businessType = BusinessType.ACCOMMODATION_AND_FOOD_SERVICES;
        }else if (string.toUpperCase().equals("RETAIL TRADE")){
            businessType = BusinessType.RETAIL_TRADE;
        }else if (string.toUpperCase().equals("CHARITABLE ORGANISATION")){
            businessType = BusinessType.CHARITABLE_ORGANISATION;
        }else{
            businessType = BusinessType.NON_PROFIT_ORGANISATION;
        }
        return businessType;
    }
}
