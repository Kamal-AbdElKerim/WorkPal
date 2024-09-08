public class Notifications {

    private int notification_id;
    private int user_id;
    private String message;
    private String notification_type;
    private String sent_at;
    private String status;

    public Notifications(int notification_id, int user_id, String message, String notification_type, String sent_at, String status) {
        this.notification_id = notification_id;
        this.user_id = user_id;
        this.message = message;
        this.notification_type = notification_type;
        this.sent_at = sent_at;
        this.status = status;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getSent_at() {
        return sent_at;
    }

    public void setSent_at(String sent_at) {
        this.sent_at = sent_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "notification_id=" + notification_id +
                ", user_id=" + user_id +
                ", message='" + message + '\'' +
                ", notification_type='" + notification_type + '\'' +
                ", sent_at='" + sent_at + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
