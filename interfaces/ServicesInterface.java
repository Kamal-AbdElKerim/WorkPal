package interfaces;

import java.sql.SQLException;
import java.util.Map;

import model.Service;

public interface ServicesInterface {

    Map<Integer, Service> findAllServices();

    void addServiceToSpace(int spaceId, int serviceId) throws SQLException;


}
