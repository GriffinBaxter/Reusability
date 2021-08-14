/**
 * Summary. This file contains the definition for the ProductId.
 *
 * Description. This file contains the defintion for the ProductId.
 *
 * @link   team-400/src/main/java/org/seng302/business/product/ProductId
 * @file   This file contains the definition for ProductId.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.model;

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
        this.businessId = businessId;
    }

    public String getId() {
        return id;
    }

    public Integer getBusinessId() {
        return businessId;
    }

}
