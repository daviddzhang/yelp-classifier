package yelpclassifier.outputhandler;

import java.util.List;

import yelpclassifier.restaurant.IYelpRestaurant;
import sml.Agent;
import sml.WMElement;
import yelpclassifier.yelpquery.HttpClientQuery;
import yelpclassifier.yelpquery.YelpQuery;

public class YelpQueryOutputHandler {
  public static void registerAgentWithOutputHandler(Agent agent, String queryWMEName,
                                                    List<IYelpRestaurant> searchResults) {
    agent.AddOutputHandler(queryWMEName, searchYelp, searchResults);
  }

  private static final Agent.OutputEventInterface searchYelp = new Agent.OutputEventInterface() {
    public void outputEventHandler(Object data, String agentName, String attributeName,
                                   WMElement pWmeAdded) {
      List<IYelpRestaurant> searchResults = (List<IYelpRestaurant>) data;
      searchResults.clear();
      YelpQuery queryHandler = new HttpClientQuery();
      WMElement searchParams = pWmeAdded.ConvertToIdentifier().FindByAttribute("search-parameters",
              0);
      String name =
              searchParams.ConvertToIdentifier().FindByAttribute("name", 0).GetValueAsString();
      String location =
              searchParams.ConvertToIdentifier().FindByAttribute("location", 0).GetValueAsString();


      searchResults.addAll(queryHandler.restaurantSearch(name, location));
      pWmeAdded.ConvertToIdentifier().CreateStringWME("status", "complete");
    }
  };
}
