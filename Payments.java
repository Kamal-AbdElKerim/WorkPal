public class Payments {

    private int payment_id;
    private int reservation_id;
    private float amount;
    private String payment_date;
    private String payment_method;
    private String payment_status;

    public Payments(int payment_id, int reservation_id, float amount, String payment_date, String payment_method, String payment_status) {
        this.payment_id = payment_id;
        this.reservation_id = reservation_id;
        this.amount = amount;
        this.payment_date = payment_date;
        this.payment_method = payment_method;
        this.payment_status = payment_status;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public int getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(int reservation_id) {
        this.reservation_id = reservation_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    @Override
    public String toString() {
        return "Payments{" +
                "payment_id=" + payment_id +
                ", reservation_id=" + reservation_id +
                ", amount=" + amount +
                ", payment_date='" + payment_date + '\'' +
                ", payment_method='" + payment_method + '\'' +
                ", payment_status='" + payment_status + '\'' +
                '}';
    }
}
