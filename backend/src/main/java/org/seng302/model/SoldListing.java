package org.seng302.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer", nullable = false)
    private User customer;

    @Column(name = "saleDate", nullable = false)
    private LocalDateTime saleDate;

    @Column(name = "listingDate", nullable = false)
    private LocalDateTime listingDate;

    @JoinColumn(name = "productId", nullable = false)
    private ProductId productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "bookmarks", nullable = false)
    private Integer bookmarks;

    public SoldListing(Business business, User customer, LocalDateTime listingDate, ProductId productId, Integer quantity, Double price, Integer bookmarks) {
        this.business = business;
        this.customer = customer;
        this.saleDate = LocalDateTime.now();
        this.listingDate = listingDate;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.bookmarks = bookmarks;
    }
}
