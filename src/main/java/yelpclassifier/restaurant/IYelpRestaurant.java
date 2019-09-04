package yelpclassifier.restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This interface represents a restaurant containing all the information of interest returned in a
 * Yelp API query. Should more features be added in the future, the features list can be updated
 * and new getters can get added.
 */
public interface IYelpRestaurant {
  /**
   * Gets the name of this restaurant.
   *
   * @return the name
   */
  String getName();

  /**
   * Gets the location of this restaurant.
   *
   * @return the location
   */
  String getLocation();

  /**
   * Gets a list of categories for this restaurant.
   *
   * @return the list of categories
   */
  List<String> getCategories();

  /**
   * Gets the review count of this restaurant.
   *
   * @return the review count
   */
  int getReviewCount();

  /**
   * Gets the rating of this restaurant.
   *
   * @return the rating
   */
  double getRating();

  /**
   * Gets the price of this restaurant (represented by dollar signs - 1 to 4).
   *
   * @return the price
   */
  String getPrice();

  // a list of features supported by the classifier
  List<String> features = new ArrayList<>(Arrays.asList("name", "location", "review_count",
          "categories", "rating", "price"));
}
