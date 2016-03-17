/**
 * Created by geddy on 17-3-2016.
 */
import java.util.HashMap;

public class UserPreference {
    HashMap<Integer,Double> ratings = new HashMap<Integer,Double>();
    private int UserId;

    public UserPreference(int userId,int id, double rating){
        this.UserId = userId;
        ratings.put(id,rating);
    }

    public int getUserId() {
        return UserId;
    }

    public double getRating(int id) {
        return ratings.get(id);
    }

    public void setRating(int id, double rating){
        ratings.put(id,rating);
    }

    public HashMap<Integer,Double> getRatings() {
        return ratings;
    }
}
