package org.seng302.business.product;

import java.io.Serializable;

/**
 * Class for the composite primary key of Product
 */
public class ProductId implements Serializable {

    private String id;

    private Integer businessId;

    public ProductId(){

    }

    /**
     * Constructor for ProductId.
     *
     * @param id Product code unique within the business' product catalogue.
     * @param businessId ID of the business the product belongs to.
     */
    public ProductId(String id, Integer businessId) {
        this.id = id;
        this. businessId = businessId;
    }

}
