public class Reservations {

    private int reservation_id;
    private int user_id;
    private String space_id;
    private String start_time;
    private String end_time;
    private String status;

    public Reservations(int reservation_id, int user_id, String space_id, String start_time, String end_time, String status) {
        this.reservation_id = reservation_id;
        this.user_id = user_id;
        this.space_id = space_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.status = status;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getSpace_id() {
        return space_id;
    }

    public void setSpace_id(String space_id) {
        this.space_id = space_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "reservations{" +
                "reservation_id=" + reservation_id +
                ", user_id=" + user_id +
                ", space_id='" + space_id + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
