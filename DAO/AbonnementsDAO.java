package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import DB_Conn.DB;
import interfaces.AbonnementsInterface;
import model.Abonnement;

public class AbonnementsDAO implements AbonnementsInterface {
    private DB db;

    public AbonnementsDAO(DB db) {
        this.db = db;
    }

    @Override
    public void addAbonnement(Abonnement abonnement) throws SQLException {
        String sql = "INSERT INTO abonnements (name, description, count_jour, price, user_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setString(1, abonnement.getName());
            stmt.setString(2, abonnement.getDescription());
            stmt.setInt(3, abonnement.getCountJour());
            stmt.setDouble(4, abonnement.getPrice());
            stmt.setInt(5, abonnement.getUserId());
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Abonnement> getAbonnementById(int abonnementId) throws SQLException {
        String sql = "SELECT * FROM abonnements WHERE abonnement_id = ?";
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, abonnementId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Abonnement abonnement = mapRowToAbonnement(rs);
                return Optional.of(abonnement);
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Abonnement> getAllAbonnements(int IDAuth) throws SQLException {
        String sql = "SELECT * FROM abonnements WHERE user_id = ?";
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, IDAuth);
            ResultSet rs = stmt.executeQuery();
            List<Abonnement> abonnements = new ArrayList<>();
            while (rs.next()) {
                abonnements.add(mapRowToAbonnement(rs));
            }
            return abonnements;
        }
    }

    @Override
    public void updateAbonnement(Abonnement abonnement) throws SQLException {
        String sql = "UPDATE abonnements SET name = ?, description = ?,count_jour = ?, price = ?, user_id = ? WHERE abonnement_id = ?";
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setString(1, abonnement.getName());
            stmt.setString(2, abonnement.getDescription());
            stmt.setInt(3, abonnement.getCountJour());
            stmt.setDouble(4, abonnement.getPrice());
            stmt.setInt(5, abonnement.getUserId());
            stmt.setInt(6, abonnement.getAbonnementId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteAbonnement(int abonnementId) throws SQLException {
        String sql = "DELETE FROM abonnements WHERE abonnement_id = ?";
        try (PreparedStatement stmt = db.connect().prepareStatement(sql)) {
            stmt.setInt(1, abonnementId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Abonnement mapRowToAbonnement(ResultSet rs) throws SQLException {
        int abonnementId = rs.getInt("abonnement_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        int countJour = rs.getInt("count_jour");

        double price = rs.getDouble("price");
        int userId = rs.getInt("user_id");
        Timestamp createdAt = rs.getTimestamp("created_at");

        return new Abonnement(abonnementId, name, description, countJour , price, userId);
    }
}
