


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import DB_Conn.DB;
import interfaces.FavoriteSpaceInterface;
import interfaces.Allclass.FavoriteSpace;

public class FavoriteSpaceDAO implements FavoriteSpaceInterface {
     private DB db;

    public FavoriteSpaceDAO(DB db) {
        this.db = db;
    }


    @Override
    public void addFavoriteSpace(FavoriteSpace favoriteSpace) {
        String sql = "INSERT INTO favoriteSpace (user_id, space_id, created_at) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = db.connect().prepareStatement(sql)) {
            pstmt.setInt(1, favoriteSpace.getUserId());
            pstmt.setInt(2, favoriteSpace.getSpaceId());
            pstmt.setTimestamp(3, Timestamp.valueOf(favoriteSpace.getCreatedAt()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeFavoriteSpace(int favoriteId) {
        String sql = "DELETE FROM favoriteSpace WHERE favorite_id = ?";
        try (PreparedStatement pstmt = db.connect().prepareStatement(sql)) {
            pstmt.setInt(1, favoriteId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FavoriteSpace> getFavoritesByUserId(int userId) {
        List<FavoriteSpace> favorites = new ArrayList<>();
        String sql = "SELECT * FROM favoriteSpace WHERE user_id = ?";
        try (PreparedStatement pstmt = db.connect().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int favoriteId = rs.getInt("favorite_id");
                int spaceId = rs.getInt("space_id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                // Create FavoriteSpace object
                FavoriteSpace favoriteSpace = new FavoriteSpace(favoriteId, userId, spaceId, createdAt.toLocalDateTime());
                // Add to the list
                favorites.add(favoriteSpace);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favorites;
    }

    public void displayFavoritesWithSpaces(int userId) {
        List<FavoriteSpace> favoriteSpaces = getFavoritesByUserId(userId);
        if (favoriteSpaces.isEmpty()) {
            System.out.println("You have no favorite spaces.");
        } else {
            System.out.println("Favorite Spaces:");
            for (FavoriteSpace favoriteSpace : favoriteSpaces) {
                // Retrieve space details
                String spaceSql = "SELECT * FROM spaces WHERE space_id = ?";
                try (PreparedStatement spacePstmt = db.connect().prepareStatement(spaceSql)) {
                    spacePstmt.setInt(1, favoriteSpace.getSpaceId());
                    ResultSet spaceRs = spacePstmt.executeQuery();
                    if (spaceRs.next()) {
                        String name = spaceRs.getString("name");
                        String description = spaceRs.getString("description");
                        int capacity = spaceRs.getInt("capacity");
                        boolean availability = spaceRs.getBoolean("availability");
                        double pricePerJour = spaceRs.getDouble("price_per_jour");
                        int spaceUserId = spaceRs.getInt("user_id");
                        Timestamp createdAt = spaceRs.getTimestamp("created_at");
                        
                        // Display space details
                        System.out.printf("Favorite ID: %d, Space ID: %d, Name: %s, Description: %s, Capacity: %d, Availability: %b, Price per Day: %.2f, Created At: %s%n",
                                favoriteSpace.getFavoriteId(),
                                favoriteSpace.getSpaceId(),
                                name,
                                description,
                                capacity,
                                availability,
                                pricePerJour,
                                createdAt.toLocalDateTime());
                    } else {
                        System.out.printf("Favorite ID: %d, Space ID: %d, Space details not found.%n",
                                favoriteSpace.getFavoriteId(),
                                favoriteSpace.getSpaceId());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }}

    @Override
    public FavoriteSpace getFavoriteById(int favoriteId) {
        FavoriteSpace favoriteSpace = null;
        String sql = "SELECT * FROM favoriteSpace WHERE favorite_id = ?";
        try (PreparedStatement pstmt = db.connect().prepareStatement(sql)) {
            pstmt.setInt(1, favoriteId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                int spaceId = rs.getInt("space_id");
                Timestamp createdAt = rs.getTimestamp("created_at");
                favoriteSpace = new FavoriteSpace(favoriteId, userId, spaceId, createdAt.toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoriteSpace;
    }


}

