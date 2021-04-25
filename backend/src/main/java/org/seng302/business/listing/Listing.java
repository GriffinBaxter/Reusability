package org.seng302.business.listing;

import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * Class for sale listings
 */
@Data
@NoArgsConstructor
@Entity
public class Listing {
    @Id // this field (attribute) is the table primary key
    @GeneratedValue//(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "listingId", nullable = false)
    private int listingId;

    // Foreign key: inventoryId, int

    // Many-to-one relationship

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "thumbnail_location")
    private String thumbnail_location;

    @Column (name = "start_date")
    private LocalDate start_date;

    @Column (name = "closing_date")
    private LocalDate closing_date;

    @Column (name = "expiry_date", nullable = false)
    private LocalDate expiry_date;

    @Column (name = "price_per_item")
    private double price_per_item;

    @Column (name = "total_price")
    private double total_price;

    @Column (name = "quantity", nullable = false)
    private int quantity;
}
