package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import DB_Conn.DB;

public class FeedbackDAO {

    private DB db;

    public FeedbackDAO(DB db) {
        this.db = db;
    }

    // Method to add feedback
    public void addFeedback(int userId, int spaceId, int rating, String comment) throws SQLException {
        String sql = "INSERT INTO feedback (user_id, space_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, spaceId);
            stmt.setInt(3, rating);
            stmt.setString(4, comment);
            stmt.executeUpdate();
        }
    }

    public void displayFeedbackForSpace(int spaceId) throws SQLException {
        String sql = "SELECT feedback_id, user_id, space_id, rating, comment, created_at " +
                     "FROM feedback " +
                     "WHERE space_id = ?";

        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            // Set the space_id parameter
            stmt.setInt(1, spaceId);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Feedback for Space ID " + spaceId + ":");
                System.out.printf("%-12s %-8s %-8s %-6s %-30s %-20s%n", "Feedback ID", "User ID", "Space ID", "Rating", "Comment", "Created At");
                System.out.println("--------------------------------------------------------------------------------");

                while (rs.next()) {
                    int feedbackId = rs.getInt("feedback_id");
                    int userId = rs.getInt("user_id");
                    int spaceIdFromDb = rs.getInt("space_id");
                    int rating = rs.getInt("rating");
                    String comment = rs.getString("comment");
                    Timestamp createdAt = rs.getTimestamp("created_at");

                    System.out.printf("%-12d %-8d %-8d %-6d %-30s %-20s%n", feedbackId, userId, spaceIdFromDb, rating, comment, createdAt);
                }
            }
        }
    }

}
