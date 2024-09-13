import java.sql.*;
import java.util.HashMap;

import DB_Conn.DB;
import interfaces.SubscriptionsInterface;
import interfaces.Allclass.Abonnement;
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
    public void displaySubscriptions(int userId) throws SQLException {
        String sql = "SELECT s.subscription_id, s.user_id, s.space_id, s.status, a.name, a.description, a.count_jour, a.price " +
                     "FROM subscriptions s " +
                     "JOIN abonnements a ON s.abonnement_id = a.abonnement_id " +
                     "WHERE s.user_id = ?";
    
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
    
            try (ResultSet rs = stmt.executeQuery()) {
                // Check if there are any results
                if (!rs.isBeforeFirst()) {
                    System.out.println("No subscriptions found.");
                    return;
                }
    
                // Print table header
                System.out.printf("%-15s  %-25s %-25s %-10s %-10s\n",
                        "Subscription ID",  "Name", "Description", "Price", "Count Jour");
                System.out.println(
                        "----------------------------------------------------------------------------------------------------------");
    
                // Print each row directly
                while (rs.next()) {
                    int subscriptionId = rs.getInt("subscription_id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    double price = rs.getDouble("price");
                    int countJour = rs.getInt("count_jour");
    
                    // Display the data in a formatted table
                    System.out.printf("%-15d  %-25s %-25s %-10.2f %-10d\n",
                            subscriptionId, name, description, price, countJour);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving subscriptions: " + e.getMessage());
            e.printStackTrace();
        }
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