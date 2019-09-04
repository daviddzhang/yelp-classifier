package yelpclassifier.controller;

import java.io.IOException;

import yelpclassifier.model.RestaurantClass;
import yelpclassifier.restaurant.IYelpRestaurant;
import yelpclassifier.view.IView;

/**
 * This class represents a mock controller used for testing. It contains an Appendable to which it
 * writes to whenever an operation is performed to ensure that the controller and the view have
 * proper connection. Some methods are not filled in as they are not important for testing.
 */
public class TestController implements IController, IViewFeatures {
  private final Appendable ap;

  /**
   * Constructs an instance of TestController with the given Appendable.
   *
   * @param view view to set this as its features
   * @param ap Appendable to write results to
   */
  public TestController(IView view, Appendable ap) {
    this.ap = ap;
    view.setFeatures(this);
  }

  @Override
  public void run() {

  }

  @Override
  public void search(String name, String location) {
    this.attemptAppend("Restaurant with name " + name + " at location " + location + " was searched " +
            "for.");
  }

  @Override
  public void trainRestaurant(IYelpRestaurant restaurant, String category, RestaurantClass liked) {
    this.attemptAppend("Restaurant with name " + restaurant.getName() + " at location "
            + restaurant.getLocation() + " with classification "
            + RestaurantClass.enumToString(liked) + " was trained with.");
  }

  @Override
  public RestaurantClass queryRestaurant(IYelpRestaurant restaurant, String category) {
    this.attemptAppend("Restaurant with name " + restaurant.getName() + " at location "
            + restaurant.getLocation() + " was queried.");

    return null;
  }

  @Override
  public void updateTrained() {
    this.attemptAppend("Trained examples updated.");
  }

  @Override
  public void save(String filePath) {
    this.attemptAppend("Saved at " + filePath);
  }

  @Override
  public void load(String filePath) {
    this.attemptAppend("Loaded from " + filePath);
  }

  @Override
  public void shutdown() {
    this.attemptAppend("Shutdown");
  }

  /**
   * Attempts to append the given message on to the Appendable. Throws an IllegalStateException if
   * attempt is unsuccessful.
   *
   * @param message the message to append
   * @throws IllegalStateException if appending fails
   */
  private void attemptAppend(String message) throws IllegalStateException {
    try {
      this.ap.append(message);
    } catch (IOException e) {
      throw new IllegalStateException("Attempt to append failed.");
    }
  }
}
