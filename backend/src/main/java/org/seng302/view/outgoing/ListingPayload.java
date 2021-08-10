/**
 * Summary. This file contains the definition for the ListingPayload.
 *
 * Description. This file contains the defintion for the ListingPayload.
 *
 * @link   team-400/src/main/java/org/seng302/business/listing/ListingPayload
 * @file   This file contains the definition for ListingPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.outgoing;

import org.seng302.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Listing Payload
 *
 * Listing payload to send to the API (outgoing).
 */
public class ListingPayload {
    private Integer id;
    private InventoryItemPayload inventoryItem;
    private Integer quantity;
    private Double price;
    private String moreInfo;
    private String created;
    private String closes;

    /**
     * Translate a list of Listing to a list of ListingPayload
     * @param listings a list of listings
     * @return a list of ListingPayload
     */
    public static List<ListingPayload> toListingPayload (List<Listing> listings) {
        List<ListingPayload> listingPayloads = new ArrayList<>();
        ListingPayload listingPayload;
        for (Listing listing: listings) {

            Product product = listing.getInventoryItem().getProduct();
            ProductPayload productPayload = new ProductPayload(
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getManufacturer(),
                    product.getRecommendedRetailPrice(),
                    product.getCreated(),
                    product.getImages(),
                    product.getBarcode()
            );

            InventoryItem inventoryItem = listing.getInventoryItem();
            InventoryItemPayload inventoryItemPayload = new InventoryItemPayload(
                    inventoryItem.getId(),
                    productPayload,
                    inventoryItem.getQuantity(),
                    inventoryItem.getPricePerItem(),
                    inventoryItem.getTotalPrice(),
                    inventoryItem.getManufactured().toString(),
                    inventoryItem.getSellBy().toString(),
                    inventoryItem.getBestBefore().toString(),
                    inventoryItem.getExpires().toString()
            );

            listingPayload = new ListingPayload(
                    listing.getId(),
                    inventoryItemPayload,
                    listing.getQuantity(),
                    listing.getPrice(),
                    listing.getMoreInfo(),
                    listing.getCreated().toString(),
                    listing.getCloses().toString()
            );

            listingPayloads.add(listingPayload);
        }

        return listingPayloads;
    }

    public ListingPayload(int id,
                          InventoryItemPayload inventoryItem,
                          Integer quantity,
                          Double price,
                          String moreInfo,
                          String created,
                          String closes
                          ) {
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
    public InventoryItemPayload getInventoryItem() {
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
    public String getCreated() {
        return created;
    }
    public String getCloses() {
        return closes;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id + "," +
                "\"inventoryItem\":" +
                "{\"id\":" + inventoryItem.getId() + "," +
                "\"product\":{" +
                "\"id\":\"" + inventoryItem.getProduct().getId() + "\"," +
                "\"name\":\"" + inventoryItem.getProduct().getName() + "\"," +
                "\"description\":\"" + inventoryItem.getProduct().getDescription() + "\"," +
                "\"manufacturer\":\"" + inventoryItem.getProduct().getManufacturer() + "\"," +
                "\"recommendedRetailPrice\":" + inventoryItem.getProduct().getRecommendedRetailPrice() + "," +
                "\"created\":\"" + inventoryItem.getProduct().getCreated() + "\"}," +
                "\"quantity\":" + inventoryItem.getQuantity() + "," +
                "\"pricePerItem\":" + inventoryItem.getPricePerItem() + "," +
                "\"totalPrice\":" + inventoryItem.getTotalPrice() + "," +
                "\"manufactured\":\"" + inventoryItem.getManufactured() + "\"," +
                "\"sellBy\":\"" + inventoryItem.getSellBy() + "\"," +
                "\"bestBefore\":\"" + inventoryItem.getBestBefore() + "\"," +
                "\"expires\":\"" + inventoryItem.getExpires() + "\"}," +
                "\"quantity\":" + quantity + "," +
                "\"price\":" + price + "," +
                "\"moreInfo\":\"" + moreInfo + "\"," +
                "\"created\":\"" + created + "\"," +
                "\"closes\":\"" + closes + "\"}";
    }
}
