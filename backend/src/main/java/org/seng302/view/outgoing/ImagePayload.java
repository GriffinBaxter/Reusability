package org.seng302.view.outgoing;

import org.seng302.model.Image;

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

    public static List<ImagePayload> convertToImagePayload(List<Image> images) {
        List<ImagePayload> payloads = new ArrayList<>();
        if (images != null) {
            for (Image image : images) {
                ImagePayload newPayload = new ImagePayload(
                        image.getId(), image.getFilename(), image.getThumbnailFilename(), image.getIsPrimary()
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

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                "\"filename\":\"" + filename + "\"," +
                "\"thumbnailFilename\":\"" + thumbnailFilename + "\"," +
                "\"isPrimary\":" + isPrimary + "," +
                " }";
    }
}
