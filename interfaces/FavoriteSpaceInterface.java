package interfaces;
import java.util.List;

import model.FavoriteSpace;

public interface FavoriteSpaceInterface {
    void addFavoriteSpace(FavoriteSpace favoriteSpace);
    void removeFavoriteSpace(int favoriteId);
    List<FavoriteSpace> getFavoritesByUserId(int userId);
    FavoriteSpace getFavoriteById(int favoriteId);
}
