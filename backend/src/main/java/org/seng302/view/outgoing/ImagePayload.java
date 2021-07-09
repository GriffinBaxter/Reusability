package org.seng302.view.outgoing;

import org.seng302.model.Image;

import java.util.ArrayList;
import java.util.List;

public class ImagePayload {

    private String path;
    private String filename;
    private String extension;

    /**
     * Constructor for image payloads.
     * @param path
     * @param filename
     * @param extension
     */
    public ImagePayload(String path, String filename, String extension) {
        this.path = path;
        this.filename = filename;
        this.extension = extension;

    }

    public static List<ImagePayload> convertToImagePayload(List<Image> images) {
        List<ImagePayload> payloads = new ArrayList<>();
        for (Image image : images) {
            ImagePayload newPayload = new ImagePayload(image.getPath(), image.getFileName(), image.getExtension());
            payloads.add(newPayload);
        }
        return payloads;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
