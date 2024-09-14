package interfaces;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import model.Abonnement;

public interface AbonnementsInterface {
    void addAbonnement(Abonnement abonnement) throws SQLException;
    Optional<Abonnement> getAbonnementById(int abonnementId) throws SQLException;
    List<Abonnement> getAllAbonnements(int IDAuth) throws SQLException;
    void updateAbonnement(Abonnement abonnement) throws SQLException;
    void deleteAbonnement(int abonnementId) throws SQLException;
}
