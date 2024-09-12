package interfaces;

import java.util.List;

import interfaces.Allclass.ServiceReservation;

public interface ServiceReservationsInterface {
      void addServiceReservation(ServiceReservation serviceReservation);
    ServiceReservation getServiceReservationById(int serviceReservationId);
    List<ServiceReservation> getAllServiceReservations();
    void updateServiceReservation(ServiceReservation serviceReservation);
    void deleteServiceReservation(int serviceReservationId);
}
