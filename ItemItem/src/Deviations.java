/**
 * Created by geddy on 25/03/16.
 */
public class Deviations {
    double rating;
    int productID;
    int amountOfRatings;

    public Deviations(int productID,double rating){
        this.productID = productID;
        this.rating = rating;
        this.amountOfRatings = 1;
    }

    public double getRating() {
        return rating;
    }

    public int getProductID() {
        return productID;
    }

    public int getAmountOfRatings() {
        return amountOfRatings;
    }

    public void updateDeviaton(UserPreference user){

        double tempDeviation = rating * amountOfRatings;

    }
}
