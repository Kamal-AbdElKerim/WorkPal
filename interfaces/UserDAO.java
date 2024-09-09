package interfaces;

import interfaces.Allclass.User;

import java.util.HashMap;
import java.util.Optional;


public interface UserDAO {

    void addUser(User user);

    HashMap<String, Object> getUserById(int id);

    Optional<User> getUserByEmail(String email);


    HashMap<Integer, User> getAllUsersByRole(int authID, String role);

    boolean updateUser(User user);

    boolean deleteUser(int id);

    void resetPassword(int user_id, String newPassword);
}
