package yelpclassifier.view;

import java.util.List;
import java.util.Map;

import yelpclassifier.controller.IViewFeatures;
import yelpclassifier.restaurant.IYelpRestaurant;
import yelpclassifier.model.RestaurantClass;

/**
 * This interface represents the view in the MVC pattern. It is responsible for displaying visual
 * components and taking in low-level user input. It communicates with the controller through
 * IViewFeatures. It receives information in the form of various setters, so views should have
 * some way of keeping track of the incoming information.
 */
public interface IView {
  /**
   * Starts displaying the visual component of the application.
   */
  void start();

  /**
   * Sets the features that this view can perform through IViewFeatures.
   *
   * @param features features that this view can perform
   */
  void setFeatures(IViewFeatures features);

  /**
   * Sets the search results that the model received from querying the Yelp API.
   *
   * @param results search results from a business search
   */
  void setSearchResults(List<IYelpRestaurant> results);

  /**
   * Sets the restaurants that have been trained so far.
   *
   * @param trained trained restaurants so far
   */
  void setTrainedSoFar(Map<IYelpRestaurant, RestaurantClass> trained);
}
