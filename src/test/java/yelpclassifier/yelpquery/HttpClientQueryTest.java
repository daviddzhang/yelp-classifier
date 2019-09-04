package yelpclassifier.yelpquery;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import yelpclassifier.restaurant.IYelpRestaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the parseBusinessInfo method of HttpClientQuery to ensure correctness when parsing
 * individual restaurants received from the Yelp API.
 */
public class HttpClientQueryTest {
  HttpClientQuery testQuery = new HttpClientQuery();

  JSONObject aRestaurant = new JSONObject("{\"id\": \"tmbW6_dJs5-UXhvROO093Q\", \"alias\": " +
          "\"casa-d-paco-newark\", \"name\": \"Casa d'Paco\", \"image_url\": \"https://s3-media3" +
          ".fl.yelpcdn.com/bphoto/MlYqy3dkehDQ8pw9e35JRA/o.jpg\", \"is_closed\": false, \"url\": " +
          "\"https://www.yelp.com/biz/casa-d-paco-newark?adjust_creative=CfHtC5fItiYPM-mZPVMnyQ" +
          "&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=CfHtC5fItiYPM" +
          "-mZPVMnyQ\", \"review_count\": 582, \"categories\": [{\"alias\": \"spanish\", " +
          "\"title\": \"Spanish\"}, {\"alias\": \"tapas\", \"title\": \"Tapas Bars\"}, " +
          "{\"alias\": \"wine_bars\", \"title\": \"Wine Bars\"}], \"rating\": 4.5, " +
          "\"coordinates\": {\"latitude\": 40.72529, \"longitude\": -74.16309}, \"transactions\":" +
          " [], \"price\": \"$$\", \"location\": {\"address1\": \"73 Warwick St\", \"address2\": " +
          "\"\", \"address3\": \"\", \"city\": \"Newark\", \"zip_code\": \"07105\", \"country\": " +
          "\"US\", \"state\": \"NJ\", \"display_address\": [\"73 Warwick St\", \"Newark, NJ " +
          "07105\"]}, \"phone\": \"+18623079466\", \"display_phone\": \"(862) 307-9466\", " +
          "\"distance\": 1325.543384776949}");

  @Test
  public void testParseBusinessInfoNull() {
    assertThrows(NullPointerException.class, () -> {
      this.testQuery.parseBusinessInfo(null);
    });
  }

  @Test
  public void testParseBusinessInfo() {
    IYelpRestaurant res = this.testQuery.parseBusinessInfo(aRestaurant);

    assertEquals("Casa d'Paco", res.getName());
    assertEquals("Newark, NJ", res.getLocation());
    assertEquals("$$", res.getPrice());
    assertEquals(new ArrayList<>(Arrays.asList("Spanish", "Tapas Bars", "Wine Bars")),
            res.getCategories());
    assertEquals(4.5, res.getRating());
    assertEquals(582, res.getReviewCount());
  }
}
