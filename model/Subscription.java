package model;
import java.sql.Timestamp;

public class Subscription {
    private int subscriptionId;
    private int userId;
    private int abonnementId;
    private int spaceId;
    private String status;
    private Timestamp createdAt;

    // Constructor
    public Subscription(int subscriptionId, int userId, int abonnementId, int spaceId, String status, Timestamp createdAt) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.abonnementId = abonnementId;
        this.spaceId = spaceId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Subscription( int userId, int abonnementId, int spaceId, String status, Timestamp createdAt) {
        this.userId = userId;
        this.abonnementId = abonnementId;
        this.spaceId = spaceId;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAbonnementId() {
        return abonnementId;
    }

    public void setAbonnementId(int abonnementId) {
        this.abonnementId = abonnementId;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId=" + subscriptionId +
                ", userId=" + userId +
                ", abonnementId=" + abonnementId +
                ", spaceId=" + spaceId +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
