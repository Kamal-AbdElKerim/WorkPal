package interfaces;
import interfaces.Allclass.FavoriteSpace;
import java.util.List;

public interface FavoriteSpaceInterface {
    void addFavoriteSpace(FavoriteSpace favoriteSpace);
    void removeFavoriteSpace(int favoriteId);
    List<FavoriteSpace> getFavoritesByUserId(int userId);
    FavoriteSpace getFavoriteById(int favoriteId);
}
