package org.seng302.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Class for Sold listings
 * This stores information about sales that have been completed in the past.
 * May be used for analysis in the future. This means we don't want to change the details
 * i.e. modify the records.
 */
@Data
@NoArgsConstructor
@Entity
public class SoldListing {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer", nullable = false)
    private User customer;

    @Column(name = "saleDate", nullable = false)
    private Date saleDate;

    @Column(name = "listingDate", nullable = false)
    private Date listingDate;

    @JoinColumn(name = "productId", nullable = false)
    private ProductId productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "bookmarks", nullable = false)
    private Integer bookmarks;
}
