package org.seng302.model;

import javax.persistence.*;

@Entity // declare this class as a JPA entity (that can be mapped to a SQL table)
public class Notification {

    @Id // this field (attribute) is the table primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autoincrement the ID
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "receiver_id")
    private Integer receiverId;

    @Column(name = "notification_message")
    private String notificationMessage;

    @Column(name = "extra_message")
    private String extraMessage;

    /**
     * Notification Notification
     * @param receiverId receiver's id
     * @param notificationMessage notification message
     * @param extraMessage extra message
     */
    public Notification(Integer receiverId, String notificationMessage, String extraMessage) {
        this.receiverId = receiverId;
        this.notificationMessage = notificationMessage;
        this.extraMessage = extraMessage;
    }

    public Notification() {
    }

    public int getId() {
        return id;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    //TODO: Test
    public String expiryFormTOString() {
        return notificationMessage + " in " + extraMessage + ".";
    }
}
