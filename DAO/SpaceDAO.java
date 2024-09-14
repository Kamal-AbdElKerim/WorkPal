package DAO;
import DB_Conn.DB;
import interfaces.Spaces;
import model.Space;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceDAO implements Spaces {

    private DB db;

    public SpaceDAO(DB db) {
        this.db = db;
    }

    @Override
    public void addSpace(Space space) {
        String sql = "INSERT INTO spaces (name, description, capacity, availability, price_per_jour, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = db.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, space.getName());
            stmt.setString(2, space.getDescription());
            stmt.setInt(3, space.getCapacity());
            stmt.setBoolean(4, space.getAvailability());
            stmt.setFloat(5, space.getPricePerJour());
            stmt.setInt(6, space.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSpace(Space space) {
        String sql = "UPDATE spaces SET name = ?, description = ?, capacity = ?, availability = ?, price_per_jour = ?, user_id = ? WHERE space_id = ?";
        try (Connection connection = db.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, space.getName());
            stmt.setString(2, space.getDescription());
            stmt.setInt(3, space.getCapacity());
            stmt.setBoolean(4, space.getAvailability());
            stmt.setFloat(5, space.getPricePerJour());
            stmt.setInt(6, space.getUserId());
            stmt.setInt(7, space.getSpaceId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpace(int spaceId) {
        String sql = "DELETE FROM spaces WHERE space_id = ?";
        try (Connection connection = db.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, spaceId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Space findSpaceById(int spaceId) {
        String sql = "SELECT * FROM spaces WHERE space_id = ?";
        try (Connection connection = db.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, spaceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSpace(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Space> findAllSpacesByGestionnaire(int userId) {
        List<Space> spaces = new ArrayList<>();
        String sql = "SELECT * FROM spaces WHERE user_id = ?";

        try (Connection connection = db.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    spaces.add(mapRowToSpace(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding spaces by gestionnaire", e);
        }

        return spaces;
    }

    @Override
    public List<Space> findSpacesByFeature(String feature) {
        List<Space> spaces = new ArrayList<>();
        String sql = "SELECT * FROM spaces WHERE features LIKE ?";
        try (Connection connection = db.connect();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + feature + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    spaces.add(mapRowToSpace(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spaces;
    }

    @Override
    public List<Space> findAvailableSpaces() {
        List<Space> spaces = new ArrayList<>();
        String sql = "SELECT * FROM spaces WHERE availability = true";
        try (Connection connection = db.connect();
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                spaces.add(mapRowToSpace(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spaces;
    }

    private Space mapRowToSpace(ResultSet rs) throws SQLException {
        Space space = new Space();
        space.setSpaceId(rs.getInt("space_id"));
        space.setName(rs.getString("name"));
        space.setDescription(rs.getString("description"));
        space.setCapacity(rs.getInt("capacity"));
        space.setAvailability(rs.getBoolean("availability"));
        space.setPricePerJour(rs.getFloat("price_per_jour"));
        space.setUserId(rs.getInt("user_id"));
        return space;
    }

    

    public void displayAllSpacesWithServices(int authId) throws SQLException {
        String sql = "SELECT s.space_id, s.name AS space_name, s.description AS space_description, " +
                "sv.name AS service_name, sv.description AS service_description " +
                "FROM spaces s " +
                "LEFT JOIN service_spaces ss ON s.space_id = ss.spaces_id " +
                "LEFT JOIN services sv ON ss.service_id = sv.service_id " +
                "WHERE s.user_id = ?  " + // Added a space before ORDER BY
                "ORDER BY s.space_id";

        try (Connection conn = db.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, authId);
            try (ResultSet rs = pstmt.executeQuery()) {
                // Map to store space details and concatenate services
                Map<Integer, StringBuilder> spaceServices = new HashMap<>();

                // Print table headers with clear separation lines
                System.out.printf("%-10s | %-20s | %-30s ",
                        "Space ID", "Space Name", "Space Description");
                System.out.println(
                        "\n------------------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    int spaceId = rs.getInt("space_id");
                    String spaceName = rs.getString("space_name");
                    String spaceDescription = rs.getString("space_description");
                    String serviceName = rs.getString("service_name");
                    String serviceDescription = rs.getString("service_description");

                    // Initialize services info if not already done for this space
                    spaceServices.putIfAbsent(spaceId,
                            new StringBuilder(String.format("%-20s | %-30s", spaceName, spaceDescription)));

                    // Add services to the space

                    if (serviceName != null && serviceDescription != null) {
                        spaceServices.get(spaceId)
                                .append(String.format("%n%-10s : %-20s", serviceName, serviceDescription));
                    } else {
                        spaceServices.get(spaceId).append(String.format("%n%-10s : %-20s", "N/A", "N/A"));
                    }

                }

                // Print all spaces with services in a styled table format
                for (Map.Entry<Integer, StringBuilder> entry : spaceServices.entrySet()) {

                    System.out.printf("%-10d | %-80s%n", entry.getKey(), entry.getValue().toString());
                    System.out.println(
                            "------------------------------------------------------------------------------------------------------------");

                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve spaces with services: " + e.getMessage());
        }
    }

    public void displayAllSpacesWithServices() throws SQLException {
        String sql = "SELECT s.space_id, s.name AS space_name, s.description AS space_description, " +
                "sv.name AS service_name, sv.description AS service_description " +
                "FROM spaces s " +
                "LEFT JOIN service_spaces ss ON s.space_id = ss.spaces_id " +
                "LEFT JOIN services sv ON ss.service_id = sv.service_id " +
                "WHERE availability = true " + // Added a space before ORDER BY
                "ORDER BY s.space_id";

        try (Connection conn = db.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                // Map to store space details and concatenate services
                Map<Integer, StringBuilder> spaceServices = new HashMap<>();

                // Print table headers with clear separation lines
                System.out.printf("%-10s | %-20s | %-30s ",
                        "Space ID", "Space Name", "Space Description");
                System.out.println(
                        "\n------------------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    int spaceId = rs.getInt("space_id");
                    String spaceName = rs.getString("space_name");
                    String spaceDescription = rs.getString("space_description");
                    String serviceName = rs.getString("service_name");
                    String serviceDescription = rs.getString("service_description");

                    // Initialize services info if not already done for this space
                    spaceServices.putIfAbsent(spaceId,
                            new StringBuilder(String.format("%-20s | %-30s", spaceName, spaceDescription)));

                    // Add services to the space

                    if (serviceName != null && serviceDescription != null) {
                        spaceServices.get(spaceId)
                                .append(String.format("%n%-10s : %-20s", serviceName, serviceDescription));
                    } else {
                        spaceServices.get(spaceId).append(String.format("%n%-10s : %-20s", "N/A", "N/A"));
                    }

                }

                // Print all spaces with services in a styled table format
                for (Map.Entry<Integer, StringBuilder> entry : spaceServices.entrySet()) {

                    System.out.printf("%-10d | %-80s%n", entry.getKey(), entry.getValue().toString());
                    System.out.println(
                            "------------------------------------------------------------------------------------------------------------");

                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve spaces with services: " + e.getMessage());
        }
    }


    public List<Space> searchSpaces(String name, Integer capacity, Double pricePerJour) {
        String sql = "SELECT * FROM spaces WHERE " +
                     "(name ILIKE ? OR ? IS NULL) " +
                     "AND (capacity = ? OR ? IS NULL) " +
                     "AND (price_per_jour = ? OR ? IS NULL)";
        
        List<Space> spaces = new ArrayList<>();
        
        try (Connection conn = db.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Set parameters with type information
            stmt.setString(1, name != null ? "%" + name + "%" : null); // Search for name (optional)
            stmt.setString(2, name); // To check if name is null
            stmt.setObject(3, capacity, java.sql.Types.INTEGER); // Search for capacity (optional)
            stmt.setObject(4, capacity, java.sql.Types.INTEGER); // To check if capacity is null
            stmt.setObject(5, pricePerJour, java.sql.Types.DOUBLE); // Search for price_per_jour (optional)
            stmt.setObject(6, pricePerJour, java.sql.Types.DOUBLE); // To check if price_per_jour is null
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                // Map the result set to Space objects
                Space space = new Space(
                    rs.getInt("space_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getInt("capacity"),
                    rs.getBoolean("availability"),
                    rs.getFloat("price_per_jour"),
                    rs.getInt("user_id"),
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
                spaces.add(space);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return spaces;
    }
    

}
