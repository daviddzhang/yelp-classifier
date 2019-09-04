package yelpclassifier.yelpquery;

import java.util.List;

import yelpclassifier.restaurant.IYelpRestaurant;

/**
 * This interface represents all the operations a class should perform if it is to query the Yelp
 * API. Regardless of how the class queries the API, it should return a list of restaurants for
 * searching, and a restaurant for looking up based on ID.
 */
public interface YelpQuery {

  /**
   * Query the Yelp API for a business search with the given name and location. Details of how
   * this is accomplished should be specified by specific implementations.
   *
   * @param name name of the restaurant
   * @param location location of the restaurant
   * @return a list of restaurants representing the search result
   */
  List<IYelpRestaurant> restaurantSearch(String name, String location);

  /**
   * Query the Yelp API for a business lookup with the given ID. Details of how this is
   * accomplished should be specified by specific implementations.
   * @param id of the restaurant
   * @return the restaurant corresponding to the ID
   */
  IYelpRestaurant restaurantLookup(String id);
}
