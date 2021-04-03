package org.seng302.business;

import java.io.Serializable;

public class ProductId implements Serializable {

    private String productCode;

    private Integer businessId;

    public ProductId(){

    }

    public ProductId(String productCode, Integer businessId) {
        this.productCode = productCode;
        this. businessId = businessId;
    }

}
