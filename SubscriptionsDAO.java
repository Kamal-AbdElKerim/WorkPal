import java.sql.*;
import java.util.HashMap;

import DB_Conn.DB;
import interfaces.SubscriptionsInterface;
import interfaces.Allclass.Subscription;

public class SubscriptionsDAO implements SubscriptionsInterface {
       private DB db;

    public SubscriptionsDAO(DB db) {
        this.db = db;
    }

    @Override
    public void addSubscription(Subscription subscription) throws SQLException {
        String sql = "INSERT INTO subscriptions (user_id, abonnement_id, space_id, status, created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = db.connect(); // Establish connection using db.connect()
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subscription.getUserId());
            stmt.setInt(2, subscription.getAbonnementId());
            stmt.setInt(3, subscription.getSpaceId());
            stmt.setString(4, subscription.getStatus());
            stmt.setTimestamp(5, subscription.getCreatedAt());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateSubscription(Subscription subscription) throws SQLException {
        String sql = "UPDATE subscriptions SET user_id = ?, abonnement_id = ?, space_id = ?, status = ?, created_at = ? WHERE subscription_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subscription.getUserId());
            stmt.setInt(2, subscription.getAbonnementId());
            stmt.setInt(3, subscription.getSpaceId());
            stmt.setString(4, subscription.getStatus());
            stmt.setTimestamp(5, subscription.getCreatedAt());
            stmt.setInt(6, subscription.getSubscriptionId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteSubscription(int subscriptionId) throws SQLException {
        String sql = "DELETE FROM subscriptions WHERE subscription_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subscriptionId);
            stmt.executeUpdate();
        }
    }
    

    @Override
    public Subscription getSubscriptionById(int subscriptionId) throws SQLException {
        String sql = "SELECT * FROM subscriptions WHERE subscription_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, subscriptionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSubscription(rs);
                }
            }
        }
        return null; // Return null if no subscription is found
    }
    
    @Override
    public HashMap<Integer, Subscription> getAllSubscriptions() throws SQLException {
        HashMap<Integer, Subscription> subscriptions = new HashMap<>();
        String sql = "SELECT * FROM subscriptions";
        try (Connection connection = db.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Subscription subscription = mapRowToSubscription(rs);
                subscriptions.put(subscription.getSubscriptionId(), subscription);
            }
        }
        return subscriptions;
    }
    

    @Override
    public HashMap<Integer, Subscription> getSubscriptionsByUserId(int userId) throws SQLException {
        HashMap<Integer, Subscription> subscriptions = new HashMap<>();
        String sql = "SELECT * FROM subscriptions WHERE user_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Subscription subscription = mapRowToSubscription(rs);
                    subscriptions.put(subscription.getSubscriptionId(), subscription);
                }
            }
        }
        return subscriptions;
    }
    
    @Override
    public HashMap<Integer, Subscription> getSubscriptionsBySpaceId(int spaceId) throws SQLException {
        HashMap<Integer, Subscription> subscriptions = new HashMap<>();
        String sql = "SELECT * FROM subscriptions WHERE space_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, spaceId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Subscription subscription = mapRowToSubscription(rs);
                    subscriptions.put(subscription.getSubscriptionId(), subscription);
                }
            }
        }
        return subscriptions;
    }
    

    private Subscription mapRowToSubscription(ResultSet rs) throws SQLException {
        return new Subscription(
            rs.getInt("subscription_id"),
            rs.getInt("user_id"),
            rs.getInt("abonnement_id"),
            rs.getInt("space_id"),
            rs.getString("status"),
            rs.getTimestamp("created_at")
        );
    }
}