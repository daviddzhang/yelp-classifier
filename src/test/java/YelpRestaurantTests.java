import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import yelpclassifier.restaurant.YelpRestaurantImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the methods of YelpRestaurantImpl
 */
public class YelpRestaurantTests {
  private Map<String, Object> constructor;

  @BeforeEach
  public void init() {
    constructor = new HashMap<>();
  }

  @Test
  public void testNegativeRating() {
    assertThrows(IllegalArgumentException.class, () -> {
      new YelpRestaurantImpl("Restaurant", "Location",
              new ArrayList<>(), "$$", -3, 100);
    });
  }

  @Test
  public void testNegativeReviewCount() {
    assertThrows(IllegalArgumentException.class, () -> {
      new YelpRestaurantImpl("Restaurant", "Location",
              new ArrayList<>(), "$$", 3, -1);
    });
  }

  @Test
  public void testAllFieldsConstructor() {
    assertEquals("Restaurant | Location", new YelpRestaurantImpl("Restaurant", "Location",
            new ArrayList<>(), "$$", 4, 100).toString());
  }

  @Test
  public void testNullMap() {
    assertThrows(IllegalArgumentException.class, () -> {
      new YelpRestaurantImpl(null);
    });
  }

  @Test
  public void testNoName() {
    this.constructor.put("location", "Location");
    this.constructor.put("categories", new ArrayList<>());
    this.constructor.put("price", "$$");
    this.constructor.put("rating", 4.3);
    this.constructor.put("review_count", 100);
    assertThrows(IllegalArgumentException.class, () -> {
      new YelpRestaurantImpl(this.constructor);
    });
  }

  @Test
  public void testNoLoc() {
    this.constructor.put("name", "Name");
    this.constructor.put("categories", new ArrayList<>());
    this.constructor.put("price", "$$");
    this.constructor.put("rating", 4.3);
    this.constructor.put("review_count", 100);
    assertThrows(IllegalArgumentException.class, () -> {
      new YelpRestaurantImpl(this.constructor);
    });
  }

  @Test
  public void testNoCategories() {
    this.constructor.put("name", "Name");
    this.constructor.put("location", "Location");
    this.constructor.put("price", "$$");
    this.constructor.put("rating", 4.3);
    this.constructor.put("review_count", 100);
    assertThrows(IllegalArgumentException.class, () -> {
      new YelpRestaurantImpl(this.constructor);
    });
  }

  @Test
  public void testNoPrice() {
    this.constructor.put("name", "Name");
    this.constructor.put("location", "Location");
    this.constructor.put("categories", new ArrayList<>());
    this.constructor.put("rating", 4.3);
    this.constructor.put("review_count", 100);
    assertThrows(IllegalArgumentException.class, () -> {
      new YelpRestaurantImpl(this.constructor);
    });
  }

  @Test
  public void testNoRating() {
    this.constructor.put("name", "Name");
    this.constructor.put("location", "Location");
    this.constructor.put("categories", new ArrayList<>());
    this.constructor.put("price", "$$");
    this.constructor.put("review_count", 100);
    assertThrows(IllegalArgumentException.class, () -> {
      new YelpRestaurantImpl(this.constructor);
    });
  }

  @Test
  public void testNoReviewCount() {
    this.constructor.put("name", "Name");
    this.constructor.put("location", "Location");
    this.constructor.put("categories", new ArrayList<>());
    this.constructor.put("price", "$$");
    this.constructor.put("rating", 4.3);
    assertThrows(IllegalArgumentException.class, () -> {
      new YelpRestaurantImpl(this.constructor);
    });
  }
}
