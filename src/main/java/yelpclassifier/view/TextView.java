package yelpclassifier.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import yelpclassifier.controller.IViewFeatures;
import yelpclassifier.restaurant.IYelpRestaurant;
import yelpclassifier.model.RestaurantClass;

/**
 * This view displays information through the supplied Readable and Appendable. It serves as a
 * quick test/demo view using System.in and out. As such, this view will not be tested nor
 * documented thoroughly.
 */
public class TextView implements IView {
  private final Readable rd;
  private final Appendable ap;
  private List<IYelpRestaurant> searchRes;
  private Map<IYelpRestaurant, RestaurantClass> trainedSoFar;
  private IViewFeatures features;
  private boolean isTraining = true;

  /**
   * Constructs a TextView object that reads with the given Readable, and outputs with the
   * given Appendable.
   *
   * @param rd the object to take user input
   * @param ap the object to transmit/output game information
   * @throws IllegalArgumentException when either or both rd and ap are null
   */
  public TextView(Readable rd, Appendable ap) throws IllegalArgumentException {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Readable and/or Appendable objects cannot be null.");
    }

    this.rd = rd;
    this.ap = ap;
  }


  @Override
  public void start() {
    Scanner scanner = new Scanner(this.rd);
    while (true) {
      if (isTraining) {
        this.attemptAppend("Currently training ... Press 1 to stop training and to query. Press q" +
                " to quit.\n");
        this.attemptAppend("Restaurant name: ");
        String name = scanner.nextLine();
        if (didUserQuit(name)) {
          break;
        }
        else if (name.equals("1")) {
          this.isTraining = false;
        }
        else {
          this.attemptAppend("Restaurant location (formatted *city*, *state/country*): ");
          String location = scanner.nextLine();
          this.features.search(name, location);
          this.attemptAppend("Search results for supplied name and location:\n");
          this.attemptAppend(this.printSearchResults());
          this.attemptAppend("Please enter the number associated with your restaurant.\n");
          int idx = Integer.parseInt(scanner.nextLine()) - 1;
          IYelpRestaurant selected = this.searchRes.get(idx);

          String category = null;
          if (selected.getCategories().size() > 1) {
            this.attemptAppend("Please select the category (using the displayed number) that best " +
                    "matches your restaurant:\n");
            this.attemptAppend(this.displayRestaurantCategories(selected.getCategories()));
            int categoryIDX = Integer.parseInt(scanner.nextLine()) - 1;
            category = selected.getCategories().get(categoryIDX);
          } else {
            category = selected.getCategories().get(0);
          }

          this.attemptAppend("Indicate whether the restaurant was good, okay, or bad.");
          try {
            RestaurantClass restClass = RestaurantClass.stringToEnum(scanner.nextLine());
            this.attemptAppend("Training...\n");
            this.features.trainRestaurant(selected, category, restClass);
            this.features.updateTrained();
            this.attemptAppend(this.printTrainedSoFar());
          } catch (IllegalArgumentException e) {
            this.attemptAppend(e.getMessage());
          }
        }
      }
      else {
        this.attemptAppend("Currently querying ... Press 1 to stop querying and to train. Press q" +
                " to quit.\n");
        this.attemptAppend("Restaurant name: ");
        String name = scanner.nextLine();
        if (didUserQuit(name)) {
          break;
        }
        else if (name.equals("1")) {
          this.isTraining = true;
        }
        else {
          this.attemptAppend("Restaurant location (formatted *city*, *state/country*): ");
          String location = scanner.nextLine();
          this.features.search(name, location);
          this.attemptAppend("Search results for supplied name and location:\n");
          this.attemptAppend(this.printSearchResults());
          this.attemptAppend("Please enter the number associated with your restaurant.\n");
          int idx = Integer.parseInt(scanner.nextLine()) - 1;
          IYelpRestaurant selected = this.searchRes.get(idx);

          String category = null;
          if (selected.getCategories().size() > 1) {
            this.attemptAppend("Please select the category (using the displayed number) that best " +
                    "matches your restaurant:\n");
            this.attemptAppend(this.displayRestaurantCategories(selected.getCategories()));
            int categoryIDX = Integer.parseInt(scanner.nextLine()) - 1;
            category = selected.getCategories().get(categoryIDX);
          }
          else {
            category = selected.getCategories().get(0);
          }

          this.attemptAppend("Predicted: ");
          this.attemptAppend(RestaurantClass.enumToString(this.features.queryRestaurant(selected, category
                  )));
        }
      }
    }
  }

  private String printTrainedSoFar() {
    StringBuilder stringBuilder = new StringBuilder();
    for (Map.Entry<IYelpRestaurant, RestaurantClass> entry : this.trainedSoFar.entrySet()) {
      stringBuilder.append(entry.getKey().getName()).append(" | ").append(entry.getKey().getLocation())
              .append(" : ").append(RestaurantClass.enumToString(entry.getValue())).append("\n");
    }

    return stringBuilder.toString();
  }

  private String displayRestaurantCategories(List<String> categories) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < categories.size(); i++) {
      stringBuilder.append((i + 1)).append(": ").append(categories.get(i)).append("\n");
    }
    return stringBuilder.toString();
  }

  private String printSearchResults() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i  = 0; i < this.searchRes.size(); i++) {
      IYelpRestaurant cur = this.searchRes.get(i);
      stringBuilder.append((i + 1)).append(": ").append(cur.getName()).append(" | ").append(cur.getLocation()).append("\n");
    }

    return stringBuilder.toString();
  }

  @Override
  public void setFeatures(IViewFeatures features) {
    this.features = features;
  }

  @Override
  public void setSearchResults(List<IYelpRestaurant> results) {
    this.searchRes = results;
  }

  @Override
  public void setTrainedSoFar(Map<IYelpRestaurant, RestaurantClass> trained) {
    this.trainedSoFar = trained;
  }

  private boolean didUserQuit(String input) {
    return input.equalsIgnoreCase("q");
  }

  /**
   * Attempts to append the given message onto this.ap. Throws an IllegalStateException if it's
   * unsuccessful.
   *
   * @param message the message to add to this.ap
   * @throws IllegalStateException if the append is unsuccessful
   */
  private void attemptAppend(String message) {
    try {
      this.ap.append(message);
    } catch (IOException e) {
      throw new IllegalStateException("Appendable not able to return output.");
    }
  }
}
