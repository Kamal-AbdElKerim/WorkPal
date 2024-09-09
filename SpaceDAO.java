import DB_Conn.DB;
import interfaces.Allclass.Space;
import interfaces.Spaces;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpaceDAO implements Spaces {

    private DB db;

    public SpaceDAO(DB db) {
        this.db = db;
    }

    @Override
    public void addSpace(Space space)  {
        String sql = "INSERT INTO spaces (name, description, capacity, availability, price_per_hour, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, space.getName());
            stmt.setString(2, space.getDescription());
            stmt.setInt(3, space.getCapacity());
            stmt.setBoolean(4, space.isAvailability());
            stmt.setFloat(5, space.getPricePerHour());
            stmt.setInt(6, space.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSpace(Space space)  {
        String sql = "UPDATE spaces SET name = ?, description = ?, capacity = ?, availability = ?, price_per_hour = ?, user_id = ? WHERE space_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, space.getName());
            stmt.setString(2, space.getDescription());
            stmt.setInt(3, space.getCapacity());
            stmt.setBoolean(4, space.isAvailability());
            stmt.setFloat(5, space.getPricePerHour());
            stmt.setInt(6, space.getUserId());
            stmt.setInt(7, space.getSpaceId());
            stmt.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpace(int spaceId)  {
        String sql = "DELETE FROM spaces WHERE space_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, spaceId);
            stmt.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Space findSpaceById(int spaceId)  {
        String sql = "SELECT * FROM spaces WHERE space_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, spaceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSpace(rs);
                }
            }
        }catch (SQLException e) {
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
    public List<Space> findSpacesByFeature(String feature)  {
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
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spaces;
    }

    @Override
    public List<Space> findAvailableSpaces() {
        List<Space> spaces = new ArrayList<>();
        String sql = "SELECT * FROM spaces WHERE availability = TRUE";
        try (Connection connection = db.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                spaces.add(mapRowToSpace(rs));
            }
        }catch (SQLException e) {
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
        space.setPricePerHour(rs.getFloat("price_per_hour"));
        space.setUserId(rs.getInt("user_id"));
        return space;
    }

    public void displayAllSpacesWithServices(int authId) throws SQLException {
        String sql = "SELECT s.name AS space_name, s.description AS space_description, " +
                "sv.name AS service_name, sv.description AS service_description " +
                "FROM spaces s " +
                "LEFT JOIN service_spaces ss ON s.space_id = ss.spaces_id " +
                "LEFT JOIN services sv ON ss.service_id = sv.service_id " +
                "WHERE s.user_id = ? " +
                "ORDER BY s.space_id";

        try (Connection conn = db.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, authId); // Set the user_id parameter

            try (ResultSet rs = pstmt.executeQuery()) {
                // Print table headers
                System.out.printf("%-20s %-30s %-20s %-30s%n",
                        "Space Name", "Space Description",
                        "Service Name", "Service Description");
                System.out.println("-------------------------------------------------------------------------------");

                while (rs.next()) {
                    String spaceName = rs.getString("space_name");
                    String spaceDescription = rs.getString("space_description");
                    String serviceName = rs.getString("service_name");
                    String serviceDescription = rs.getString("service_description");

                    // Print each row of the table
                    System.out.printf("%-20s %-30s %-20s %-30s%n",
                            spaceName, spaceDescription,
                            (serviceName != null ? serviceName : "N/A"),
                            (serviceDescription != null ? serviceDescription : "N/A"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve spaces with services: " + e.getMessage());
        }
    }



}
