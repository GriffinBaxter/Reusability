package org.seng302.view.outgoing;

public class ImageCreatePayload {

    private Integer id;

    public ImageCreatePayload(Integer id) {
        this.id = id;
    }

    public Integer getId() { return id; }

    @Override
    public String toString() {
        return "{\"id\":" + id + " }";
    }
}
