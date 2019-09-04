package yelpclassifier.model;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import yelpclassifier.restaurant.IYelpRestaurant;

/**
 * This class represents a mock model used for testing. It contains an Appendable to which it
 * writes to whenever an operation is performed to ensure that the controller and the model have
 * proper connection. Some methods are not filled in as they are not important for testing.
 */
public class TestModel implements IModel {
  private final Appendable ap;

  /**
   * Constructs an instance of TestModel with the given Appendable.
   *
   * @param ap Appendable to write results to
   */
  public TestModel(Appendable ap) {
    this.ap = ap;
  }

  @Override
  public List<IYelpRestaurant> search(String name, String location) {
    this.attemptAppend("Model: Restaurant with name " + name + " at location " + location + " was" +
            " searched for.");

    return null;
  }

  @Override
  public void train(IYelpRestaurant restaurant, String restaurantCategory, RestaurantClass liked) {
    this.attemptAppend("Model: Restaurant with name " + restaurant.getName() + " at location "
            + restaurant.getLocation() + " with classification "
            + RestaurantClass.enumToString(liked) + " was trained with.");
  }

  @Override
  public RestaurantClass query(IYelpRestaurant restaurant, String restaurantCategory) {
    this.attemptAppend("Model: Restaurant with name " + restaurant.getName() + " at location "
            + restaurant.getLocation() + " was queried.");

    return null;
  }

  @Override
  public Map<IYelpRestaurant, RestaurantClass> getTrainedSoFar() {
    return null;
  }

  @Override
  public void save(String filePath) {
    this.attemptAppend("Model: Saved at " + filePath);
  }

  @Override
  public void load(String filePath) {
    this.attemptAppend("Model: Loaded from " + filePath);
  }

  @Override
  public void shutdown() {
    this.attemptAppend("Model: Shutdown");
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
