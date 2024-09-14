package model;

import java.sql.Timestamp;

public class Feedback {
    private int feedbackId;
    private int userId;
    private int spaceId;
    private int rating;
    private String comment;
    private Timestamp createdAt;

    // Constructor with parameters
    public Feedback(int feedbackId, int userId, int spaceId, int rating, String comment, Timestamp createdAt) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.spaceId = spaceId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    

    // Default constructor
    public Feedback() {}

    // Getters and setters
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
