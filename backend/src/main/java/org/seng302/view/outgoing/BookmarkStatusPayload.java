package org.seng302.view.outgoing;

/**
 * Payload for bookmark status.
 * Return when bookmark status updated.
 */
public class BookmarkStatusPayload {
    private boolean isBookmarked;

    public BookmarkStatusPayload(boolean isBookmarked) {
        this.isBookmarked = isBookmarked;
    }

    public BookmarkStatusPayload() {
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }
}
