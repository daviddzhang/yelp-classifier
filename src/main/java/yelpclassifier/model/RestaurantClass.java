package yelpclassifier.model;

/**
 * The classification for a restaurant based on whether it was liked or not. The possible
 * enumerations are GOOD, OKAY, and BAD. Methods exist that convert from string to enum, and vice
 * versa.
 */
public enum RestaurantClass {
  GOOD, OKAY, BAD;

  /**
   * Converts the given string to the corresponding enum - throws exception if the string is
   * invalid.
   *
   * @param s string to convert
   * @return the correct enum
   * @throws IllegalArgumentException if the string does not convert to any of the enums
   */
  public static RestaurantClass stringToEnum(String s) throws IllegalArgumentException {
    if (s == null) {

      throw new IllegalArgumentException("String cannot be null");
    }
    switch (s.toLowerCase()) {
      case "good":
        return GOOD;
      case "okay":
        return OKAY;
      case "bad":
        return BAD;
      default:
        throw new IllegalArgumentException("Given class is not supported.");
    }
  }

  /**
   * Converts the given RestaurantClass enum to a lowercase string.
   *
   * @param c enum to convert to string
   * @return the string corresponding to the enum
   */
  public static String enumToString(RestaurantClass c) {
    if (c == null) {
      throw new IllegalArgumentException("Class cannot be null");
    }

    switch (c) {
      case BAD:
        return "bad";
      case GOOD:
        return "good";
      case OKAY:
        return "okay";
      default:
        throw new IllegalArgumentException("Given enum type is not supported.");
    }
  }
}
