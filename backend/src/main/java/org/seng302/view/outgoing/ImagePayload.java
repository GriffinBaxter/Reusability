package org.seng302.view.outgoing;

import org.seng302.model.ProductImage;

import java.util.ArrayList;
import java.util.List;

public class ImagePayload {

    private Integer id;
    private String filename;
    private String thumbnailFilename;
    private boolean isPrimary;

    public ImagePayload(Integer id, String filename, String thumbnailFilename, boolean isPrimary) {
        this.id = id;
        this.filename = filename;
        this.thumbnailFilename = thumbnailFilename;
        this.isPrimary = isPrimary;
    }

    public static List<ImagePayload> convertToImagePayload(List<ProductImage> productImages) {
        List<ImagePayload> payloads = new ArrayList<>();
        if (productImages != null) {
            for (ProductImage productImage : productImages) {
                ImagePayload newPayload = new ImagePayload(
                        productImage.getId(), productImage.getFilename(), productImage.getThumbnailFilename(), productImage.getIsPrimary()
                );
                payloads.add(newPayload);
            }
        }
        return payloads;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getThumbnailFilename() {
        return thumbnailFilename;
    }

    public void setThumbnailFilename(String thumbnailFilename) {
        this.thumbnailFilename = thumbnailFilename;
    }

    public boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
