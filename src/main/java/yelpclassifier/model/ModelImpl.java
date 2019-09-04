package yelpclassifier.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yelpclassifier.outputhandler.YelpQueryOutputHandler;
import yelpclassifier.restaurant.IYelpRestaurant;
import reasoningmodels.outputhandlers.ReasoningModels;
import sml.Agent;
import sml.Identifier;
import sml.Kernel;
import sml.WMElement;

/**
 * This model implementation loads in rules from a .soar file in the resources folder. It also
 * makes use of the SoarReasoningModels.ReasoningModels methods to register output handlers. The
 * reasoning model information is also held in ReasoningModels. Upon construction, the agent is
 * created and registered with the output handlers. It also initializes the necessary reasoning
 * models.
 */
public class ModelImpl implements IModel {
  private static Map<String, String> YELP_CATEGORIES;
  private final Agent agent;
  private final Kernel kernel;
  private final List<IYelpRestaurant> currentSearchList;
  private final Map<IYelpRestaurant, RestaurantClass> trainedSoFar;

  /**
   * Constructs an instance of ModelImpl. This initializes the agent and reasoning model, as well
   * as loads in a set of predefined support categories from Yelp's API.
   */
  public ModelImpl() {
    this.trainedSoFar = new HashMap<>();
    this.currentSearchList = new ArrayList<>();
    this.kernel = Kernel.CreateKernelInCurrentThread(true);
    this.agent = kernel.CreateAgent("yelp-classifier");
    agent.LoadProductions(new File(ModelImpl.class.getResource("/agent/yelp-agent.soar")
            .getFile()).getAbsolutePath());
    ReasoningModels.addReasoningOutputHandlersToAgent(this.agent,
            (Serializable) this.trainedSoFar, "create",
            "training", "query-handler");
    YelpQueryOutputHandler.registerAgentWithOutputHandler(this.agent,
            "search", this.currentSearchList);

    Identifier init = this.agent.GetInputLink().CreateIdWME("init");
    agent.RunSelf(2);
    init.DestroyWME();
    agent.RunSelf(2);

    try {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      InputStream is = classloader.getResourceAsStream("yelp_categories.ser");
      ObjectInputStream read = new ObjectInputStream(is);
      YELP_CATEGORIES = (Map<String, String>) read.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

  /**
   * The output handler for searches mutates this class's search list field, so on top of
   * returning a new search list, that field is also mutated.
   */
  @Override
  public List<IYelpRestaurant> search(String name, String location) {
    Identifier il = this.agent.GetInputLink();
    WMElement nameID = il.CreateStringWME("name", name);
    WMElement locID = il.CreateStringWME("location", location);
    agent.RunSelf(2);
    nameID.DestroyWME();
    locID.DestroyWME();
    agent.RunSelf(2);
    return new ArrayList<>(this.currentSearchList);
  }

  /**
   * Training examples are stored in a list of reasoning models in ReasoningModels. The client
   * should have no way of accessing these models, except to print them.
   */
  @Override
  public void train(IYelpRestaurant restaurant, String restaurantCategory, RestaurantClass liked) {
    if (restaurant == null || restaurantCategory == null) {
      throw new IllegalArgumentException("Given restaurant or category cannot be null.");
    }

    if (liked == null) {
      throw new IllegalArgumentException("Given classification cannot be null");
    }
    Identifier il = this.agent.GetInputLink();
    Identifier trainID = il.CreateIdWME("training");
    String categoryAlias = ModelImpl.YELP_CATEGORIES.get(restaurantCategory);
    this.unsupportedCategoryCheck(categoryAlias);
    this.writeRestaurantInfoToWME(restaurant, categoryAlias, trainID);
    trainID.CreateStringWME("liked", RestaurantClass.enumToString(liked));
    agent.RunSelf(2);
    trainID.DestroyWME();
    agent.RunSelf(2);
    this.trainedSoFar.put(restaurant, liked);
  }

  /**
   * Checks if the given category is a supported category.
   *
   * @param categoryAlias category to check
   */
  private void unsupportedCategoryCheck(String categoryAlias) {
    if (categoryAlias == null) {
      throw new IllegalArgumentException("Given category is not supported - select another " +
              "category.");
    }
  }

  @Override
  public RestaurantClass query(IYelpRestaurant restaurant, String restaurantCategory) {
    if (restaurant == null || restaurantCategory == null) {
      throw new IllegalArgumentException("Given restaurant or category cannot be null.");
    }

    Identifier il = this.agent.GetInputLink();
    Identifier queryID = il.CreateIdWME("query");
    String categoryAlias = ModelImpl.YELP_CATEGORIES.get(restaurantCategory);
    this.unsupportedCategoryCheck(categoryAlias);
    this.writeRestaurantInfoToWME(restaurant, categoryAlias, queryID);
    agent.RunSelf(2);
    String resString = agent.GetOutputLink().FindByAttribute("query-handler", 0)
            .ConvertToIdentifier().FindByAttribute("result", 0).GetValueAsString();
    queryID.DestroyWME();
    agent.RunSelf(2);
    return RestaurantClass.stringToEnum(resString);
  }

  @Override
  public Map<IYelpRestaurant, RestaurantClass> getTrainedSoFar() {
    return new HashMap<>(this.trainedSoFar);
  }

  @Override
  public void save(String filePath) {
    try {
      ReasoningModels.serialize(filePath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void load(String filePath) {
    try {
      this.trainedSoFar.clear();
      this.trainedSoFar.putAll(
              (Map<IYelpRestaurant, RestaurantClass>) ReasoningModels.deserialize(filePath));
      this.currentSearchList.clear();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void shutdown() {
    this.kernel.Shutdown();
  }

  /**
   * Writes the supported features to the given WME.
   *
   * @param restaurant restaurant whose features to write on the WME
   * @param restaurantCategory category of the restaurant
   * @param idToWriteTo WME to write to
   */
  private void writeRestaurantInfoToWME(IYelpRestaurant restaurant, String restaurantCategory,
                                        Identifier idToWriteTo) {
    idToWriteTo.CreateStringWME("category", restaurantCategory);
    idToWriteTo.CreateStringWME("price", restaurant.getPrice());
    idToWriteTo.CreateFloatWME("rating", restaurant.getRating());
    idToWriteTo.CreateIntWME("review-count", restaurant.getReviewCount());
  }
}
