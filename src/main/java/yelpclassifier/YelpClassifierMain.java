package yelpclassifier;

import yelpclassifier.controller.ControllerImpl;
import yelpclassifier.controller.IController;
import yelpclassifier.model.IModel;
import yelpclassifier.model.ModelImpl;
import yelpclassifier.view.IView;
import yelpclassifier.view.SwingView;

/**
 * This class contains the main method to run the application.
 */
public class YelpClassifierMain {
  public static void main(String[] args) {
    IModel model = new ModelImpl();
    //IView view = new TextView(new BufferedReader(new InputStreamReader(System.in)), System.out);
    IView view = new SwingView();
    IController controller = new ControllerImpl(model, view);

    controller.run();
  }
}
