package org.seng302.business.product.images;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.seng302.business.product.Product;

import javax.persistence.*;
/**
 * Image entity class for images.
 */
@Data   // Generates getters and setters
@NoArgsConstructor // Generaters the JPA requried empty constructor
@Entity // Marks it as a table for Hirbernate?
public class Image {

    // Id field for an Image entity
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private int id;

    // Association many images can have the same one product id.
    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "product-id", referencedColumnName = "id", insertable = false, updatable = false),
        @JoinColumn(name = "business-id", referencedColumnName = "business_id", insertable = false, updatable = false)
    })
    private Product product;

    @Column(name = "product-id")
    private String productId;

    @Column(name = "business-id")
    private int bussinesId;

    // The file path stores for recovery.
    @Column(name = "path", nullable = false)
    private String path;

    // The file name in case we need to resolve it.
    @Column(name = "file-name", nullable = false)
    private String fileName;

    // Extension type of the file
    @Column(name = "extension", nullable = false)
    private String extension;

    /**
     * A constructor for when you have an ID
     *
     * @param id
     * @param path
     * @param fileName
     * @param extension
     */
    public Image(int id, String path, String fileName, String extension) {
        this.id = id;
        this.path = path;
        this.fileName = fileName;
        this.extension = extension;
    }

    /**
     * A constructor for when you don't have an ID
     *
     * @param path
     * @param fileName
     * @param extension
     */
    public Image(String path, String fileName, String extension) {
        this.path = path;
        this.fileName = fileName;
        this.extension = extension;
    }

}
