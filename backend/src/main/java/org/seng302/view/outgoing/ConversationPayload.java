package org.seng302.view.outgoing;

import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;

import java.time.LocalDateTime;

/**
 * ConversationPayload class, used to send conversation data to the frontend.
 */
public class ConversationPayload {

    private int id;
    private int instigatorId;
    private String instigatorName;
    private ImagePayload instigatorImage;
    private int receiverId;
    private String receiverName;
    private ImagePayload receiverImage;
    private int marketplaceCardId;
    private String marketplaceCardTitle;
    private String created;
    private boolean deletedByInstigator;
    private boolean deletedByReceiver;

    /**
     * Constructor for conversation payloads.
     *
     * @param id The ID of the conversation.
     * @param instigator The User object representing the instigator of the conversation.
     * @param receiver The User object representing the recipient of the conversation.
     * @param marketplaceCard The MarketplaceCard object representing the marketplace card the conversation refers to.
     * @param created The date and time the conversation was created.
     * @param deletedByInstigator A boolean which keeps track of whether the instigator of the conversation has deleted/left
     *                            the conversation. true if they have deleted/left, false otherwise.
     * @param deletedByReceiver A boolean which keeps track of whether the receiver of the conversation has deleted/left
     *                          the conversation. true if they have deleted/left, false otherwise.
     */
    public ConversationPayload(
            int id,
            User instigator,
            User receiver,
            MarketplaceCard marketplaceCard,
            LocalDateTime created,
            boolean deletedByInstigator,
            boolean deletedByReceiver
    ) {
        this.id = id;
        this.instigatorId = instigator.getId();
        this.instigatorName = instigator.getFirstName() + " " +
                              (instigator.getMiddleName() != null && !instigator.getMiddleName().equals("") ? instigator.getMiddleName() + " ": "") +
                              instigator.getLastName();
        //TODO: add instigator image when images are implemented for users
        this.receiverId = receiver.getId();
        this.receiverName = receiver.getFirstName() + " " +
                            (receiver.getMiddleName() != null && !receiver.getMiddleName().equals("") ? receiver.getMiddleName() + " ": "") +
                            receiver.getLastName();
        //TODO: add receiver image when images are implemented for users
        this.marketplaceCardId = marketplaceCard.getId();
        this.marketplaceCardTitle = marketplaceCard.getTitle();
        this.created = created.toString();
        this.deletedByInstigator = deletedByInstigator;
        this.deletedByReceiver = deletedByReceiver;
    }

    public int getId() {
        return id;
    }

    public int getInstigatorId() {
        return instigatorId;
    }

    public String getInstigatorName() {
        return instigatorName;
    }

    public ImagePayload getInstigatorImage() {
        return instigatorImage;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public ImagePayload getReceiverImage() {
        return receiverImage;
    }

    public int getMarketplaceCardId() {
        return marketplaceCardId;
    }

    public String getMarketplaceCardTitle() {
        return marketplaceCardTitle;
    }

    public String getCreated() {
        return created;
    }

    public boolean getDeletedByInstigator() {  return deletedByInstigator; }

    public boolean getDeletedByReceiver() { return deletedByReceiver; }

    /**
     * An override of the toString method for debugging and testing purposes.
     * @return String representation of the conversation payload.
     */
    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ",\"instigatorId\":" + instigatorId +
                ",\"instigatorName\":\"" + instigatorName + "\"," +
                "\"instigatorImage\":" + instigatorImage +
                ",\"receiverId\":" + receiverId +
                ",\"receiverName\":\"" + receiverName + "\"," +
                "\"receiverImage\":" + receiverImage +
                ",\"marketplaceCardId\":" + marketplaceCardId +
                ",\"marketplaceCardTitle\":\"" + marketplaceCardTitle + "\"," +
                "\"created\":\"" + created + "\"," +
                "\"deletedByInstigator\":" + deletedByInstigator + "," +
                "\"deletedByReceiver\":" + deletedByReceiver + "}";
    }
}
