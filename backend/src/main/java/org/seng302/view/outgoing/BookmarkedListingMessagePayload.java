package org.seng302.view.outgoing;

import org.seng302.model.BookmarkedListingMessage;

import java.time.LocalDateTime;

/**
 * Payload for the BookmarkedListingMessage.
 */
public class BookmarkedListingMessagePayload {

    private Integer id;
    private String description;
    private String created;
    private String listingCloses;
    private Integer listingId;
    private Integer businessId;


    /**
     * Payload for sending a bookmarked message to the front-end when a user bookmarks or un-bookmarks a sale listing.
     * @param id Message ID
     * @param description Message Description
     * @param created Time message was created
     * @param listingCloses Listing closing date
     * @param listingId Listing ID
     * @param businessId Business ID for business who listed the listing
     */
    public BookmarkedListingMessagePayload(Integer id,
                                           String description,
                                           LocalDateTime created, LocalDateTime listingCloses, Integer listingId, Integer businessId) {
        this.id = id;
        this.description = description;
        this.created = created.toString();
        this.listingCloses = listingCloses.toString();
        this.listingId = listingId;
        this.businessId = businessId;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated() {
        return created;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreated(String created) {
        this.created = created;
    }


    public String getCloses() {
        return listingCloses;
    }

    public void setCloses(String closes) {
        this.listingCloses = closes;
    }

    public Integer getListingId() {
        return listingId;
    }

    public void setListingId(Integer listingId) {
        this.listingId = listingId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    /**
     * Override the toString method for debugging purposes.
     * @return a string representing the BookmarkedListingMessagePayload.
     */
    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"description\":\"" + description + "\"" +
                ",\"created\":\"" + created + "\"" +
                ",\"listingCloses\":\"" + listingCloses + "\"" +
                ",\"listingId\":\"" + listingId + "\"" +
                ",\"businessId\":\"" + businessId + "\"" +
                "}";
    }

}
