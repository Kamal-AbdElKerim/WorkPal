package interfaces;

import interfaces.Allclass.Service;

import java.sql.SQLException;
import java.util.Map;

public interface ServicesInterface {

    Map<Integer, Service> findAllServices();

    void addServiceToSpace(int spaceId, int serviceId) throws SQLException;


}
