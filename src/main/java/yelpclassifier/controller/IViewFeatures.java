package yelpclassifier.controller;

import yelpclassifier.model.RestaurantClass;
import yelpclassifier.restaurant.IYelpRestaurant;

/**
 * This interface represents the possible features that a view might want to implement. A view
 * should contain an instance of a class belonging to this interface to communicate with the
 * controller in an immutable way.
 */
public interface IViewFeatures {
  /**
   * Query the Yelp API with the given name and location. Should have an effect on the model.
   *
   * @param name name of the restaurant
   * @param location location of the restaurant
   */
  void search(String name, String location);

  /**
   * Adds the supplied arguments as a training example in the model.
   *
   * @param restaurant restaurant to train with
   * @param category restaurant's category
   * @param liked its classification
   */
  void trainRestaurant(IYelpRestaurant restaurant, String category, RestaurantClass liked);

  /**
   * Queries the model with the supplied arguments and returns a classification.
   *
   * @param restaurant restaurant to train with
   * @param category restaurant's category
   * @return a classification of whether or not the restaurant will be good
   */
  RestaurantClass queryRestaurant(IYelpRestaurant restaurant, String category);

  /**
   * Update the view's record of the trained examples so far using the model.
   */
  void updateTrained();

  /**
   * Save the current reasoning model state to the supplied location.
   *
   * @param filePath location to save the model
   */
  void save(String filePath);

  /**
   * Load a reasoning model state from the supplied location.
   *
   * @param filePath location to load the model from
   */
  void load(String filePath);

  /**
   * Shuts down the kernel in the model for proper termination.
   */
  void shutdown();
}
