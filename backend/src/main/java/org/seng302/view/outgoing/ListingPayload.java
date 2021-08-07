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

public class ListingPayload {

    private Integer id;
    private InventoryItemPayload inventoryItem;
    private Integer quantity;
    private Double price;
    private String moreInfo;
    private String created;
    private String closes;
    private boolean isBookmarked;
    private Integer totalBookmarks;

    public ListingPayload(int id,
                          InventoryItemPayload inventoryItem,
                          Integer quantity,
                          Double price,
                          String moreInfo,
                          String created,
                          String closes,
                          boolean isBookmarked,
                          Integer totalBookmarks
                          ) {
    this.id = id;
    this.inventoryItem = inventoryItem;
    this.quantity = quantity;
    this.price = price;
    this.moreInfo = moreInfo;
    this.created = created;
    this.closes = closes;
    this.isBookmarked = isBookmarked;
    this.totalBookmarks = totalBookmarks;
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

    public boolean getIsBookmarked() {
        return isBookmarked;
    }

    public Integer getTotalBookmarks() {
        return totalBookmarks;
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
                "\"created\":\"" + inventoryItem.getProduct().getCreated() + "\"" +
                "\"business\":" +
                "{\"id\":" + inventoryItem.getProduct().getBusiness().getId() + "," +
                "\"administrators\":" + inventoryItem.getProduct().getBusiness().getAdministrators() + "," +
                "\"primaryAdministratorId\":" + inventoryItem.getProduct().getBusiness().getPrimaryAdministratorId() + "," +
                "\"name\":\"" + inventoryItem.getProduct().getBusiness().getName() + "\"," +
                "\"description\":\"" + inventoryItem.getProduct().getBusiness().getDescription() + "\"," +
                "\"address\":" + inventoryItem.getProduct().getBusiness().getAddress() + "," +
                "\"businessType\":\"" + inventoryItem.getProduct().getBusiness().getBusinessType() + "\"," +
                "\"created\":\"" + inventoryItem.getProduct().getBusiness().getCreated() + "\"}}," +
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
                "\"closes\":\"" + closes + "\"," +
                "\"isBookmarked\":" + isBookmarked + "," +
                "\"totalBookmarks\":" + totalBookmarks + "}";
    }
}
