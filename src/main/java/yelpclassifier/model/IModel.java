package yelpclassifier.model;

import java.util.List;
import java.util.Map;

import yelpclassifier.restaurant.IYelpRestaurant;

/**
 * This interface represents the model for a Yelp classifier application. It is responsible for
 * various operations, from querying the Yelp API for restaurants to saving/loading previous
 * states. Implementations should utilize a Soar agent that has various output handlers and rules
 * loaded into it to perform its necessary operations.
 */
public interface IModel {
  /**
   * Returns a list of restaurants that result from querying the Yelp API with the given name and
   * location. If the query is unsuccessful, an exception will be thrown.
   *
   * @param name of the restaurant
   * @param location location of the restaurant
   * @return list of restaurants corresponding to the search result
   */
  List<IYelpRestaurant> search(String name, String location);

  /**
   * Trains the model with the given restaurant, its category, and whether it was liked or not.
   * How the model keeps track of the examples is up to the implementation and should be documented.
   *
   * @param restaurant restaurant to train
   * @param restaurantCategory the restaurant's category
   * @param liked whether the user liked it or not based on the possible enums
   */
  void train(IYelpRestaurant restaurant, String restaurantCategory, RestaurantClass liked);

  /**
   * Queries whether the restaurant in question was liked or not. Returns a RestaurantClass
   * enumeration.
   *
   * @param restaurant restaurant in question
   * @param restaurantCategory category of restaurant
   * @return whether it was liked or not
   */
  RestaurantClass query(IYelpRestaurant restaurant, String restaurantCategory);

  /**
   * Get the trained restaurants so far in the form of a map, pairing each restaurant with
   * whether it was liked or not.
   *
   * @return a mapping of restaurants to whether they were liked
   */
  Map<IYelpRestaurant, RestaurantClass> getTrainedSoFar();

  /**
   * Serializes the necessary model information to the given filepath. An error will be thrown if
   * the filepath is invalid.
   *
   * @param filePath where to save the file
   */
  void save(String filePath);

  /**
   * Deserializes the necessary model information from the given filepath. An error will be
   * thrown if the filepath is invalid.
   *
   * @param filePath where to load the file from
   */
  void load(String filePath);

  /**
   * Shuts down the kernel for the model.
   */
  void shutdown();
}
