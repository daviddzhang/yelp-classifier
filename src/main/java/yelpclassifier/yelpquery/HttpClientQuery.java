package yelpclassifier.yelpquery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import yelpclassifier.restaurant.IYelpRestaurant;
import yelpclassifier.restaurant.YelpRestaurantImpl;

/**
 * This class implements the YelpQuery interface. It queries the Yelp API using HttpClient. The
 * API key and URI components are static variables.
 */
public class HttpClientQuery implements YelpQuery {

  private static String API_KEY = APIKey.API_KEY; //key goes here

  private static String API_HOST = "https://api.yelp.com";
  private static String SEARCH_PATH = "/v3/businesses/search";
  private static String BUSINESS_PATH = "/v3/businesses/";

  /**
   * Constructs an instance of HttpClientQuery.
   */
  public HttpClientQuery() {

  }

  /**
   * A 10 restaurant limit is set to the search. Exceptions will be thrown if connection is
   * unsuccessful.
   */
  @Override
  public List<IYelpRestaurant> restaurantSearch(String name, String location) {
    HttpClient client = HttpClient.newHttpClient();

    URI uri = appendUri(API_HOST + SEARCH_PATH, "term=" + name);
    uri = appendUri(uri.toString(), "location=" + location);
    uri = appendUri(uri.toString(), "limit=10");

    HttpRequest request =
            HttpRequest.newBuilder().header("Authorization", "Bearer " + API_KEY).uri(uri).GET().build();

    try {
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      return parseBodyForRestaurants(response.body());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public IYelpRestaurant restaurantLookup(String id) {
    HttpClient client = HttpClient.newHttpClient();
    URI uriIDSearch = this.attemptConstructURI(API_HOST + BUSINESS_PATH + id);

    HttpRequest idRequest =
            HttpRequest.newBuilder().header("Authorization", "Bearer " + API_KEY).uri(uriIDSearch).GET().build();

    try {
      HttpResponse<String> response = client.send(idRequest, HttpResponse.BodyHandlers.ofString());
      return parseBusinessInfo(new JSONObject(response.body()));
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Parses a singular business in the form of a JSONObject into an IYelpRestaurant. Handles the
   * different cases for each feature.
   *
   * @param businessInfo Yelp API query result for a business
   * @return a restaurant representing to the same business
   */
  IYelpRestaurant parseBusinessInfo(JSONObject businessInfo) {
    Objects.requireNonNull(businessInfo);
    Map<String, Object> features = new HashMap<>();
    for (String feature : IYelpRestaurant.features) {
      // handle special case for "categories" feature
      if (feature.equalsIgnoreCase("categories")) {
        // create a list of String for each category
        JSONArray categoryArray = businessInfo.getJSONArray("categories");
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < categoryArray.length(); i++) {
          categories.add(categoryArray.getJSONObject(i).getString("title"));
        }
        features.put("categories", categories);
      }
      // handle special case for "location" feature
      else if (feature.equalsIgnoreCase("location")) {
        JSONObject locationObj = businessInfo.getJSONObject(feature);
        // if state is provided, use state, otherwise, use country
        if (locationObj.getString("state").equals("")) {
          features.put(feature, locationObj.getString("city") + ", " + locationObj.getString(
                  "country"));
        }
        else {
          features.put(feature, locationObj.getString("city") + ", " + locationObj.getString(
                  "state"));
        }
      }
      // if no special case, put in map as object
      else {
        // some restaurants do not specify price - in that case, put an empty string
        try {
          features.put(feature, businessInfo.get(feature));
        }
        catch (JSONException e) {
          features.put(feature, "");
        }
      }
    }

    return new YelpRestaurantImpl(features);
  }

  /**
   * Constructs a list of restaurants with the returned Yelp API query for a business search.
   *
   * @param responseBody all businesses from a business search
   * @return a list of restaurants representing the returned businesses
   */
  private List<IYelpRestaurant> parseBodyForRestaurants(String responseBody) {
    Objects.requireNonNull(responseBody);
    JSONObject searchRes = new JSONObject(responseBody);
    JSONArray resultList = searchRes.getJSONArray("businesses");
    List<IYelpRestaurant> res = new ArrayList<>();
    for (int i = 0; i < resultList.length(); i++) {
      res.add(this.parseBusinessInfo(resultList.getJSONObject(i)));
    }

    return res;
  }

  /**
   * Adds a new query to a URI depending on whether it already contains queries.
   *
   * @param uri existing URI to add to
   * @param appendQuery query to add
   * @return a new URI with the query appended to it
   */
  private URI appendUri(String uri, String appendQuery) {
    URI oldUri = this.attemptConstructURI(uri);

    String newQuery = oldUri.getQuery();
    if (newQuery == null) {
      newQuery = appendQuery;
    } else {
      newQuery += "&" + appendQuery;
    }

    URI newUri = null;

    try {
      newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
              oldUri.getPath(), newQuery, oldUri.getFragment());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }

    return newUri;
  }

  /**
   * Attempts to construct a URI with the given string
   *
   * @param uri URI to construct
   * @return a new URI
   */
  private URI attemptConstructURI(String uri) {
    try {
      return new URI(uri);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return null;
  }
}
