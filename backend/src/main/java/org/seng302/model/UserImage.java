package org.seng302.model;

import org.seng302.model.enums.ImageType;

import javax.persistence.*;

@Entity
public class UserImage extends Image {
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id", nullable = false)
    private int userId;

    public UserImage(Integer id, int userId, String filename, String thumbnailFilename, Boolean isPrimary) {
        super(id, filename, thumbnailFilename, isPrimary, ImageType.USER_IMAGE);
        this.userId = userId;
    }

    public UserImage(int userId, String filename, String thumbnailFilename, Boolean isPrimary) {
        super(filename, thumbnailFilename, isPrimary, ImageType.USER_IMAGE);
        this.userId = userId;
    }

    public UserImage() {}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + getId() + ", " +
                "\"userId\":" + userId + ", " +
                "\"filename\":\"" + getFilename() + "\", " +
                "\"thumbnailFilename\":\"" + getThumbnailFilename() + "\", " +
                "\"isPrimary\":" + getIsPrimary() +
                "}";
    }
}
