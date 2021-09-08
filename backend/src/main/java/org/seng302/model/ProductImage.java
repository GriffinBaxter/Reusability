package org.seng302.model;

import org.seng302.model.enums.ImageType;

import javax.persistence.*;

@Entity
public class ProductImage extends Image {
    // Association many images can have the same one product id.
    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false),
            @JoinColumn(name = "business_id", referencedColumnName = "business_id", insertable = false, updatable = false)
    })
    private Product product;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "business_id", nullable = false)
    private int businessId;

    public ProductImage(int id, String productId, Integer businessId, String filename, String thumbnailFilename, boolean isPrimary) {
        super(id, filename, thumbnailFilename, isPrimary, ImageType.PRODUCT_IMAGE);
        this.productId = productId;
        this.businessId = businessId;
    }

    public ProductImage(String productId, Integer businessId, String filename, String thumbnailFilename, boolean isPrimary) {
        super(filename, thumbnailFilename, isPrimary, ImageType.PRODUCT_IMAGE);
        this.productId = productId;
        this.businessId = businessId;
    }

    public ProductImage() {
        super();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
                "\"productId\":" + productId + ", " +
                "\"filename\":\"" + getFilename() + "\", " +
                "\"thumbnailFilename\":\"" + getThumbnailFilename() + "\", " +
                "\"isPrimary\":" + getIsPrimary() +
                "}";
    }
}
