package yelpclassifier.controller;

/**
 * This interface represents the controller in the MVC pattern for this application. It simply
 * contains a run method that starts the application in the view/model.
 */
public interface IController {
  /**
   * Runs the application. May perform different tasks depending on the implementation, but it
   * should always be the starting point for the application.
   */
  void run();
}
