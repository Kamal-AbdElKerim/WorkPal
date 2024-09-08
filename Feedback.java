public class Feedback {

    private int feedback_id;
    private int user_id;
    private int space_id;
    private int rating;
    private String comment;

    public Feedback(int feedback_id, int user_id, int space_id, int rating, String comment) {
        this.feedback_id = feedback_id;
        this.user_id = user_id;
        this.space_id = space_id;
        this.rating = rating;
        this.comment = comment;
    }

    public int getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(int feedback_id) {
        this.feedback_id = feedback_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSpace_id() {
        return space_id;
    }

    public void setSpace_id(int space_id) {
        this.space_id = space_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedback_id=" + feedback_id +
                ", user_id=" + user_id +
                ", space_id=" + space_id +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }
}
