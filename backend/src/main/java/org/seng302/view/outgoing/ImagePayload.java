package org.seng302.view.outgoing;

import org.seng302.model.Image;

import java.util.ArrayList;
import java.util.List;

public class ImagePayload {

    private Integer id;
    private String filename;
    private String thumbnailFilename;


    public ImagePayload(Integer id, String filename, String thumbnailFilename) {
        this.id = id;
        this.filename = filename;
        this.thumbnailFilename = thumbnailFilename;

    }

    public static List<ImagePayload> convertToImagePayload(List<Image> images) {
        List<ImagePayload> payloads = new ArrayList<>();
        for (Image image : images) {
            ImagePayload newPayload = new ImagePayload(image.getId(), image.getFilename(), image.getThumbnailFilename());
            payloads.add(newPayload);
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
}
