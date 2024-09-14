package model;
public class Service_reservations {

    private int service_reservation_id ;
    private int service_id ;
    private int reservation_id ;
    private float total_price ;

    public Service_reservations(int service_reservation_id, int service_id, int reservation_id,  float total_price) {
        this.service_reservation_id = service_reservation_id;
        this.service_id = service_id;
        this.reservation_id = reservation_id;
        this.total_price = total_price;
    }
   

    public int getService_reservation_id() {
        return service_reservation_id;
    }

    public void setService_reservation_id(int service_reservation_id) {
        this.service_reservation_id = service_reservation_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }



    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    @Override
    public String toString() {
        return "service_reservations{" +
                "service_reservation_id=" + service_reservation_id +
                ", service_id=" + service_id +
                ", reservation_id=" + reservation_id +
                ", total_price=" + total_price +
                '}';
    }
}
