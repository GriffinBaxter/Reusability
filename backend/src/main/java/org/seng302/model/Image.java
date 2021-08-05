package org.seng302.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Image {
    // Id field for an Image entity
    @Id
    @GeneratedValue
    @Column(name = "image_id", nullable = false)
    private Integer id;

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

    // The file name in case we need to resolve it.
    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "thumbnailFilename")
    private String thumbnailFilename;

    // Extension type of the file
    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary;



    public Image(int id, String productId, Integer businessId, String filename, String thumbnailFilename, boolean isPrimary) {
        this.id = id;
        this.productId = productId;
        this.businessId = businessId;
        this.filename = filename;
        this.thumbnailFilename = thumbnailFilename;
        this.isPrimary = isPrimary;
    }

    public Image(String productId, Integer businessId, String filename, String thumbnailFilename, boolean isPrimary) {
        this.productId = productId;
        this.businessId = businessId;
        this.filename = filename;
        this.thumbnailFilename = thumbnailFilename;
        this.isPrimary = isPrimary;
    }

    public String toString() {
        return "{" +
                "\"id\":\"" + id + "\"," +
                "\"filename\":\"" + filename + "\"," +
                "\"thumbnailFilename\":\"" + thumbnailFilename + "\"" +
                "\"isPrimary\":\"" + isPrimary + "\"," +
                "}";
    }

}
