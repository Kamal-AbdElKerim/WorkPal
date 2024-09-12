package interfaces.Allclass;

import java.time.LocalDateTime;

public class FavoriteSpace {
    private int favoriteId;
    private int userId;
    private int spaceId;
    private LocalDateTime createdAt;

    public FavoriteSpace(int favoriteId, int userId, int spaceId, LocalDateTime createdAt) {
        this.favoriteId = favoriteId;
        this.userId = userId;
        this.spaceId = spaceId;
        this.createdAt = createdAt;
    }
    public FavoriteSpace( int userId, int spaceId, LocalDateTime createdAt) {
        this.userId = userId;
        this.spaceId = spaceId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

