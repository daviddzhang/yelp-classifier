package yelpclassifier.controller;

import yelpclassifier.model.IModel;
import yelpclassifier.model.RestaurantClass;
import yelpclassifier.restaurant.IYelpRestaurant;
import yelpclassifier.view.IView;

/**
 * This class is an implementation of IController and IViewFeatures. Not only does it serve as the
 * starting point for the application, but it also centralizes communication between the view and
 * the model. It fulfills all the features dictated by IViewFeatures.
 */
public class ControllerImpl implements IController, IViewFeatures {
  private final IModel model;
  private final IView view;

  /**
   * Constructs an instance of ControllerImpl with the given model and view. It also sets this as
   * the features for the view.
   *
   * @param model model in the MVC
   * @param view view in the MVC
   */
  public ControllerImpl(IModel model, IView view) {
    this.model = model;
    this.view = view;
    this.view.setFeatures(this);
  }


  @Override
  public void run() {
    view.start();
  }


  @Override
  public void search(String name, String location) {
    this.view.setSearchResults(this.model.search(name, location));
  }

  @Override
  public void trainRestaurant(IYelpRestaurant restaurant, String category, RestaurantClass liked) {
    this.model.train(restaurant, category, liked);
  }

  @Override
  public RestaurantClass queryRestaurant(IYelpRestaurant restaurant, String category) {
    return this.model.query(restaurant, category);
  }

  @Override
  public void updateTrained() {
    this.view.setTrainedSoFar(this.model.getTrainedSoFar());
  }

  @Override
  public void save(String filePath) {
    this.model.save(filePath);
  }

  @Override
  public void load(String filePath) {
    try {
      this.model.load(filePath);
    } catch (Exception e) {
      throw new IllegalArgumentException("Given file cannot be found");
    }
  }

  @Override
  public void shutdown() {
    this.model.shutdown();
  }
}
