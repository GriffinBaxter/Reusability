package org.seng302.business.listing;

import org.seng302.business.inventoryItem.InventoryPayload;
import java.time.LocalDateTime;
import java.util.*;

public class ListingPayload {
    private Integer id;
    private InventoryPayload inventoryItem;
    private Integer quantity;
    private Double price;
    private String moreInfo;
    private LocalDateTime created;
    private LocalDateTime closes;

//    public static List<ListingPayload> toListingPayload (List<Listing> listings) throws Exception {
//        List<ListingPayload> listingPayloads = new ArrayList<>();
//        ListingPayload listingPayload;
//        for (Listing listing: listings) {
//            listingPayload = new ListingPayload(
//                listing.getId(),
//                listing.getInventoryItem(),
//                listing.getQuantity(),
//                listing.getPrice(),
//                listing.getMoreInfo(),
//                listing.getCreated(),
//                listing.getCloses()
//            );
//            listingPayloads.add(listingPayload);
//        }
//        return listingPayloads;
//    }

    public ListingPayload(int id,
                          InventoryPayload inventoryItem,
                          Integer quantity,
                          Double price,
                          String moreInfo,
                          LocalDateTime created,
                          LocalDateTime closes
                          ) throws Exception {
    this.id = id;
    this.inventoryItem = inventoryItem;
    this.quantity = quantity;
    this.price = price;
    this.moreInfo = moreInfo;
    this.created = created;
    this.closes = closes;
    }

    // Getters
    public int getId() {
        return id;
    }
    public InventoryPayload getInventoryItem() {
        return inventoryItem;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public Double getPrice() {
        return price;
    }
    public String getMoreInfo() {
        return moreInfo;
    }
    public LocalDateTime getCreated() {
        return created;
    }
    public LocalDateTime getCloses() {
        return closes;
    }
}