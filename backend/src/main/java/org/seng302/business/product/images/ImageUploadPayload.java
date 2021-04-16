package org.seng302.business.product.images;

import org.springframework.web.multipart.MultipartFile;

/**
 * Image upload POST request payload
 */

public class ImageUploadPayload {

    private MultipartFile[] images;

    public MultipartFile[] getImages() {
        return images;
    }

    public void setImages(MultipartFile[] images) {
        this.images = images;
    }
}
