package org.seng302.view.outgoing;

import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
import org.seng302.model.UserImage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ConversationPayload class, used to send conversation data to the frontend.
 */
public class ConversationPayload {

    private Integer id;
    private Integer instigatorId;
    private String instigatorName;
    private List<ImagePayload> instigatorImages;
    private Integer receiverId;
    private String receiverName;
    private List<ImagePayload> receiverImages;
    private Integer marketplaceCardId;
    private String marketplaceCardTitle;
    private String created;
    private boolean deletedByInstigator;
    private boolean deletedByReceiver;
    private boolean readByInstigator;
    private boolean readByReceiver;

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
            boolean deletedByReceiver,
            boolean readByInstigator,
            boolean readByReceiver
    ) {
        this.id = id;
        this.instigatorId = instigator.getId();
        this.instigatorName = instigator.getFirstName() + " " +
                              (instigator.getMiddleName() != null && !instigator.getMiddleName().equals("") ? instigator.getMiddleName() + " ": "") +
                              instigator.getLastName();
        List<UserImage> instigatorUserImages = instigator.getUserImages();
        this.instigatorImages = ImagePayload.convertToImagePayload(instigatorUserImages == null ? null : new ArrayList<>(instigatorUserImages));
        this.receiverId = receiver.getId();
        this.receiverName = receiver.getFirstName() + " " +
                            (receiver.getMiddleName() != null && !receiver.getMiddleName().equals("") ? receiver.getMiddleName() + " ": "") +
                            receiver.getLastName();
        List<UserImage> receiverUserImages = receiver.getUserImages();
        this.receiverImages = ImagePayload.convertToImagePayload(receiverUserImages == null ? null : new ArrayList<>(receiverUserImages));
        this.marketplaceCardId = marketplaceCard.getId();
        this.marketplaceCardTitle = marketplaceCard.getTitle();
        this.created = created.toString();
        this.deletedByInstigator = deletedByInstigator;
        this.deletedByReceiver = deletedByReceiver;
        this.readByInstigator = readByInstigator;
        this.readByReceiver = readByReceiver;
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

    public List<ImagePayload> getInstigatorImages() {
        return instigatorImages;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public List<ImagePayload> getReceiverImages() {
        return receiverImages;
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

    public boolean getReadByInstigator() { return readByInstigator; }

    public boolean getReadByReceiver() { return readByReceiver; }

    /**
     * An override of the toString method for debugging and testing purposes.
     * @return String representation of the conversation payload.
     */
    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ",\"instigatorId\":" + instigatorId +
                ",\"instigatorName\":\"" + instigatorName + "\"" +
                ",\"instigatorImages\":" + instigatorImages +
                ",\"receiverId\":" + receiverId +
                ",\"receiverName\":\"" + receiverName + "\"" +
                ",\"receiverImages\":" + receiverImages +
                ",\"marketplaceCardId\":" + marketplaceCardId +
                ",\"marketplaceCardTitle\":\"" + marketplaceCardTitle + "\"" +
                ",\"created\":\"" + created + "\"" +
                ",\"deletedByInstigator\":" + deletedByInstigator +
                ",\"deletedByReceiver\":" + deletedByReceiver +
                ",\"readByInstigator\":" + readByInstigator +
                ",\"readByReceiver\":" + readByReceiver + "}";
    }
}
