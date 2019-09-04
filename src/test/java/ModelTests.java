import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import reasoningmodels.IReasoningModel;
import reasoningmodels.outputhandlers.ReasoningModels;
import yelpclassifier.model.IModel;
import yelpclassifier.model.ModelImpl;
import yelpclassifier.model.RestaurantClass;
import yelpclassifier.restaurant.IYelpRestaurant;
import yelpclassifier.restaurant.YelpRestaurantImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the methods of ModelImpl. Note: there are no unit tests for the search method as it
 * requires the Yelp API.
 */
public class ModelTests {
  private IModel model;
  private final IYelpRestaurant restaurant1 = new YelpRestaurantImpl("Restaurant 1", "NJ",
          new ArrayList<>(Collections.singletonList("Category 1")), "$$", 4.3, 100);
  private final IYelpRestaurant restaurant2 = new YelpRestaurantImpl("Restaurant 2", "NJ",
          new ArrayList<>(Collections.singletonList("Category 2")), "$$$", 3.5, 12);

  @BeforeEach
  public void init() {
    try {
      Field models = ReasoningModels.class.getDeclaredField("models");
      models.setAccessible(true);
      models.set(null, new ArrayList<IReasoningModel>());
    } catch (Exception e) {
      e.printStackTrace();
    }
    model = new ModelImpl();
  }

  @Test
  public void testModelConstructor() {
    assertEquals("Model: 0\n" +
            "[rating, category, review-count, price, liked]\n" +
            "\n", ReasoningModels.printModels());
    model.shutdown();
  }

  @Test
  public void testTrainModelNullRestaurant() {
    assertThrows(IllegalArgumentException.class, () -> {
      model.train(null, "Chinese", RestaurantClass.GOOD);
    });
    model.shutdown();
  }

  @Test
  public void testTrainModelNullCategory() {
    assertThrows(IllegalArgumentException.class, () -> {
      model.train(restaurant1, null, RestaurantClass.GOOD);
    });
    model.shutdown();
  }

  @Test
  public void testTrainModelNullClassification() {
    assertThrows(IllegalArgumentException.class, () -> {
      model.train(restaurant1, "Chinese", null);
    });
    model.shutdown();
  }

  @Test
  public void testTrainInvalidCategory() {
    assertThrows(IllegalArgumentException.class, () -> {
      this.model.train(restaurant1, "Category 1", RestaurantClass.GOOD);
    });
    model.shutdown();
  }

  @Test
  public void testTrainModel() {
    this.model.train(restaurant1, "Chinese", RestaurantClass.GOOD);
    this.model.train(restaurant2, "Mexican", RestaurantClass.GOOD);

    assertEquals(RestaurantClass.GOOD, model.getTrainedSoFar().get(restaurant1));
    assertEquals(RestaurantClass.GOOD, model.getTrainedSoFar().get(restaurant2));
    assertEquals("Model: 0\n" +
            "[liked, price, category, rating, review-count]\n" +
            "[good, $$, chinese, 4.3, 100.0]\n" +
            "[good, $$$, mexican, 3.5, 12.0]\n" +
            "\n", ReasoningModels.printModels());
    model.shutdown();
  }

  @Test
  public void testQueryModelNullRestaurant() {
    assertThrows(IllegalArgumentException.class, () -> {
      model.query(null, "Chinese");
    });
    model.shutdown();
  }

  @Test
  public void testQueryModelNullCategory() {
    assertThrows(IllegalArgumentException.class, () -> {
      model.query(restaurant1, null);
    });
    model.shutdown();
  }

  @Test
  public void testQueryModelInvalidCategory() {
    assertThrows(IllegalArgumentException.class, () -> {
      model.query(restaurant1, "Category1");
    });
    model.shutdown();
  }

  @Test
  public void testQueryModel() {
    this.model.train(restaurant1, "Chinese", RestaurantClass.GOOD);
    this.model.train(restaurant2, "Mexican", RestaurantClass.GOOD);
    assertEquals(RestaurantClass.GOOD, model.query(restaurant1, "Pizza"));

    this.model.shutdown();
  }

  @Test
  public void testSaveAndLoad() {
    this.model.train(restaurant1, "Chinese", RestaurantClass.GOOD);
    this.model.train(restaurant2, "Mexican", RestaurantClass.GOOD);

    assertEquals(RestaurantClass.GOOD, model.getTrainedSoFar().get(restaurant1));
    assertEquals(RestaurantClass.GOOD, model.getTrainedSoFar().get(restaurant2));
    assertEquals("Model: 0\n" +
            "[liked, price, category, rating, review-count]\n" +
            "[good, $$, chinese, 4.3, 100.0]\n" +
            "[good, $$$, mexican, 3.5, 12.0]\n" +
            "\n", ReasoningModels.printModels());

    IYelpRestaurant testRest = new YelpRestaurantImpl("Restaurant 3", "NJ",
            new ArrayList<>(Collections.singletonList("Category 3")), "$", 3.0, 300);

    File test = new File("src/test/resources/test.ser");
    model.save(test.getAbsolutePath());

    this.model.train(testRest, "Burgers", RestaurantClass.BAD);

    assertEquals(RestaurantClass.GOOD, model.getTrainedSoFar().get(restaurant1));
    assertEquals(RestaurantClass.GOOD, model.getTrainedSoFar().get(restaurant2));
    assertEquals(RestaurantClass.BAD, model.getTrainedSoFar().get(testRest));
    assertEquals("Model: 0\n" +
            "[liked, price, category, rating, review-count]\n" +
            "[good, $$, chinese, 4.3, 100.0]\n" +
            "[good, $$$, mexican, 3.5, 12.0]\n" +
            "[bad, $, burgers, 3.0, 300.0]\n" +
            "\n", ReasoningModels.printModels());

    model.load(test.getAbsolutePath());

    assertEquals(2, model.getTrainedSoFar().entrySet().size());
    assertEquals("Model: 0\n" +
            "[liked, price, category, rating, review-count]\n" +
            "[good, $$, chinese, 4.3, 100.0]\n" +
            "[good, $$$, mexican, 3.5, 12.0]\n" +
            "\n", ReasoningModels.printModels());

    this.model.shutdown();
  }

}
