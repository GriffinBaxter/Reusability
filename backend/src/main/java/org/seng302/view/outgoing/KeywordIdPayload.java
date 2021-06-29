/**
 * Summary. This file contains the definition for the KeywordIdPayload.
 *
 * Description. This file contains the definition for the KeywordIdPayload.
 *
 * @link   team-400/src/main/java/org/seng302/business/listing/KeywordIdPayload
 * @file   This file contains the definition for KeywordIdPayload.
 * @author team-400.
 * @since  5.5.2021
 */
package org.seng302.view.outgoing;

/**
 * Id return Payload for Keyword
 */
public class KeywordIdPayload {
    private int keywordId;

    public KeywordIdPayload(int keywordId) {
        this.keywordId = keywordId;
    }

    public int getBusinessId() {
        return keywordId;
    }

    public KeywordIdPayload() {
    }
}
