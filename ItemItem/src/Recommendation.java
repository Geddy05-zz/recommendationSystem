
public class Recommendation {
    public int itemId;
    public double rating;

    public Recommendation(int itemId, double rating){
        this.itemId = itemId;
        this.rating = rating;
    }

    public int getItemId() {
        return itemId;
    }

    public double getRating() {
        return rating;
    }
}
