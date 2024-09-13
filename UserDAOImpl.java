

import DB_Conn.DB;
import interfaces.Allclass.User;
import interfaces.UserDAO;

import java.sql.*;
import java.util.HashMap;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    private DB db;

    public UserDAOImpl(DB db) {
        this.db = db;
    }

    @Override
    public  void addUser(User user) {
        String sql = "INSERT INTO users (name, email, password, address, phone_number, profile_picture, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getPhone_number());
            stmt.setString(6, user.getImg());
            stmt.setString(7, user.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int login(String email, String password) {
        String sql = "SELECT user_id FROM users WHERE email = ? AND password = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();


           if (rs.next()) {
                return rs.getInt("user_id"); // Return the user ID on successful login
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    @Override
    public HashMap<String, Object> getUserById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        HashMap<String, Object> userData = new HashMap<>();

        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                userData.put("user_id", rs.getInt("user_id"));
                userData.put("name", rs.getString("name"));
                userData.put("email", rs.getString("email"));
                userData.put("phone_number", rs.getString("phone_number"));
                userData.put("address", rs.getString("address"));
                userData.put("role", rs.getString("role"));

                return userData;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public HashMap<Integer, User> getAllUsersByRole(int authID , String role) {
        HashMap<Integer, User> usersMap = new HashMap<>();
        String sql = "SELECT * FROM users WHERE user_id != ? and role = ?";

        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, authID);
            stmt.setString(2, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getString("address"),
                        rs.getString("phone_number"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                int userId = rs.getInt("user_id");
                usersMap.put(userId, user);  // Add the user to the map with user_id as the key
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usersMap;  // Return the HashMap containing user_id and User objects
    }



    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, phone_number = ?, address = ? WHERE user_id = ?";
        System.out.println("user" + user);
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getPhone_number());
            stmt.setString(3, user.getAddress());
            stmt.setInt(4, user.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;  // Return true if at least one row was deleted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    @Override
    public void resetPassword(int user_id, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";
        try (Connection connection = db.connect();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    
  
    

    public boolean updateUserPassword(User user) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        
        try (Connection conn = db.connect(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getPassword()); // Set new password
            stmt.setString(2, user.getEmail());    // Use the user's email to find the record
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if update was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    // private User mapResultSetToUser(ResultSet rs) throws SQLException {
    //     return new User(
    //             rs.getInt("id"),
    //             rs.getString("name"),
    //             rs.getString("email"),
    //             rs.getString("password"),
    //             rs.getString("address"),
    //             rs.getString("phone_number"),
    //             rs.getString("profile_picture")
    //     );
    // }
}
