package yelpclassifier.restaurant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class contains all the information required as specified by the interface IYelpRestaurant.
 */
public class YelpRestaurantImpl implements IYelpRestaurant, Serializable {
  private final String name;
  private final String location;
  private final List<String> categories;
  private final String price;
  private final double rating;
  private final int reviewCount;

  /**
   * Constructs an instance of YelpRestaurantImpl with the given non-null fields.
   *
   * @param name name of the restaurant
   * @param location location of the restaurant
   * @param categories categories of the restaurant
   * @param price price of the restaurant
   * @param rating rating of the restaurant
   * @param reviewCount review count of the restaurant
   */
  public YelpRestaurantImpl(String name, String location, List<String> categories, String price,
                            double rating, int reviewCount) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null.");
    }

    if (location == null) {
      throw new IllegalArgumentException("Location cannot be null.");
    }

    if (categories == null) {
      throw new IllegalArgumentException("Categories cannot be null.");
    }

    if (price == null) {
      throw new IllegalArgumentException("Price cannot be null.");
    }

    if (rating < 0) {
      throw new IllegalArgumentException("Restaurants cannot have negative ratings.");
    }

    if (reviewCount < 0) {
      throw new IllegalArgumentException("Cannot have negative review counts.");
    }

    this.name = name;
    this.location = location;
    this.categories = categories;
    this.price = price;
    this.rating = rating;
    this.reviewCount = reviewCount;
  }

  /**
   * Constructs an instance of YelpRestaurantImpl with the given map. The map should contain all
   * the information a restaurant has - an exception will be thrown if it cannot find the
   * necessary info.
   *
   * @param features map containing restaurant information
   */
  public YelpRestaurantImpl(Map<String, Object> features) {
    if (features == null) {
      throw new IllegalArgumentException("Features cannot be null");
    }

    String name = (String)features.get("name");
    if (name == null) {
      throw new IllegalArgumentException("Must provide a name for the restaurant.");
    }
    this.name = name;

    String location = (String)features.get("location");
    if (location == null) {
      throw new IllegalArgumentException("Must provide a location for the restaurant.");
    }
    this.location = location;

    List<String> categories = (List<String>)features.get("categories");
    if (categories == null) {
      throw new IllegalArgumentException("Must provide categories for the restaurant.");
    }
    this.categories = categories;

    String price = (String)features.get("price");
    if (price == null) {
      throw new IllegalArgumentException("Must provide a price for the restaurant.");
    }
    this.price = price;

    Object rating = features.get("rating");
    if (rating == null) {
      throw new IllegalArgumentException("Must provide a rating for the restaurant.");
    }
    this.rating = (double)rating;

    Object reviewCount = features.get("review_count");
    if (reviewCount == null) {
      throw new IllegalArgumentException("Must provide a review count for the restaurant.");
    }
    this.reviewCount = (int)reviewCount;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public List<String> getCategories() {
    return new ArrayList<>(categories);
  }

  @Override
  public int getReviewCount() {
    return reviewCount;
  }

  @Override
  public double getRating() {
    return rating;
  }

  @Override
  public String getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return this.name + " | " + this.location;
  }
}
