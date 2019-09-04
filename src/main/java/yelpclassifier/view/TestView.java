package yelpclassifier.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import yelpclassifier.controller.IViewFeatures;
import yelpclassifier.model.RestaurantClass;
import yelpclassifier.restaurant.IYelpRestaurant;
import yelpclassifier.restaurant.YelpRestaurantImpl;

/**
 * This class represents a mock view used for testing. It contains an Readable that it reads from in
 * the start() method. Depending on the text in the Readable, it will perform a different operation
 * on its features. Some methods are not filled in as they are not important for testing.
 */
public class TestView implements IView {
  private IViewFeatures features;
  private final Readable rd;

  private static final IYelpRestaurant restaurant =
          new YelpRestaurantImpl("Restaurant", "Location",
                  new ArrayList<>(), "$$", 4, 100);

  /**
   * Constructs an instance of TestView with the given Readable.
   *
   * @param rd Readable to read from
   */
  public TestView(Readable rd) {
    this.rd = rd;
  }


  @Override
  public void start() {
    Scanner scanner = new Scanner(rd);
    String command = scanner.next();
    switch (command) {
      case "search":
        this.features.search("Name", "Location");
        break;
      case "train":
        this.features.trainRestaurant(restaurant, "Category", RestaurantClass.GOOD);
        break;
      case "query":
        this.features.queryRestaurant(restaurant, "Category");
        break;
      case "update":
        this.features.updateTrained();
        break;
      case "save":
        this.features.save("Filepath");
        break;
      case "load":
        this.features.load("Filepath");
        break;
      case "shutdown":
        this.features.shutdown();
        break;
      default:
    }
  }

  @Override
  public void setFeatures(IViewFeatures features) {
    this.features = features;
  }

  @Override
  public void setSearchResults(List<IYelpRestaurant> results) {

  }

  @Override
  public void setTrainedSoFar(Map<IYelpRestaurant, RestaurantClass> trained) {

  }
}
