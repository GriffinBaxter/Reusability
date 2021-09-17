package org.seng302.model;

import org.seng302.model.enums.ImageType;

import javax.persistence.*;

@Entity
public class BusinessImage extends Image {
    @ManyToOne(targetEntity = Business.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Business business;

    @Column(name = "business_id", nullable = false)
    private int businessId;

    public BusinessImage(int id, Integer businessId, String filename, String thumbnailFilename, boolean isPrimary) {
        super(id, filename, thumbnailFilename, isPrimary, ImageType.BUSINESS_IMAGE);
        this.businessId = businessId;
    }

    public BusinessImage(Integer businessId, String filename, String thumbnailFilename, boolean isPrimary) {
        super(filename, thumbnailFilename, isPrimary, ImageType.BUSINESS_IMAGE);
        this.businessId = businessId;
    }

    public BusinessImage() {
        super();
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public int getBusinessId() {
        return businessId;
    }

    public void setBusinessId(int businessId) {
        this.businessId = businessId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + getId() + ", " +
                "\"businessId\":" + businessId + ", " +
                "\"filename\":\"" + getFilename() + "\", " +
                "\"thumbnailFilename\":\"" + getThumbnailFilename() + "\", " +
                "\"isPrimary\":" + getIsPrimary() +
                "}";
    }
}
