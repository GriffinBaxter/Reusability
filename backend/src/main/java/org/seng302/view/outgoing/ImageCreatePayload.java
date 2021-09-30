package org.seng302.view.outgoing;

public class ImageCreatePayload {

    private Integer id;

    private String filename;

    private boolean isPrimary;

    private String thumbnailFilename;

    public ImageCreatePayload(Integer id, String filename, boolean isPrimary, String thumbnailFilename) {
        this.id = id;
        this.filename = filename;
        this.isPrimary = isPrimary;
        this.thumbnailFilename = thumbnailFilename;
    }

    public Integer getId() { return id; }
    public String getFilename() { return filename; }
    public boolean getIsPrimary() { return isPrimary; }
    public String getThumbnailFilename() { return thumbnailFilename; }

    @Override
    public String toString() {
        return "{\"id\":" + id +
                ",\"filename\":\"" + filename + "\"" +
                ",\"isPrimary\":" + isPrimary +
                ",\"thumbnailFilename\":\"" + thumbnailFilename + "\"}";
    }
}
