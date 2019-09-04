import org.junit.jupiter.api.Test;

import yelpclassifier.model.RestaurantClass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the methods of RestaurantClass.
 */
public class RestaurantClassTests {
  @Test
  public void testStringToEnumGood() {
    assertEquals(RestaurantClass.GOOD, RestaurantClass.stringToEnum("good"));
  }

  @Test
  public void testStringToEnumBad() {
    assertEquals(RestaurantClass.BAD, RestaurantClass.stringToEnum("bad"));
  }

  @Test
  public void testStringToEnumOkay() {
    assertEquals(RestaurantClass.OKAY, RestaurantClass.stringToEnum("okay"));
  }

  @Test
  public void testStringToEnumOkayCaseInsensitive() {
    assertEquals(RestaurantClass.OKAY, RestaurantClass.stringToEnum("Okay"));
  }

  @Test
  public void testStringToEnumInvalid() {
    assertThrows(IllegalArgumentException.class, () -> {
      RestaurantClass.stringToEnum("Not bad");
    });
  }

  @Test
  public void testStringToEnumNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      RestaurantClass.stringToEnum(null);
    });
  }

  @Test
  public void testEnumToStringNull() {
    assertThrows(IllegalArgumentException.class, () -> {
      RestaurantClass.enumToString(null);
    });
  }

  @Test
  public void testEnumToStringGood() {
    assertEquals("good", RestaurantClass.enumToString(RestaurantClass.GOOD));
  }

  @Test
  public void testEnumToStringBad() {
    assertEquals("bad", RestaurantClass.enumToString(RestaurantClass.BAD));
  }

  @Test
  public void testEnumToStringOkay() {
    assertEquals("okay", RestaurantClass.enumToString(RestaurantClass.OKAY));
  }
}
