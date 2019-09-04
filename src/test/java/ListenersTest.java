import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import yelpclassifier.controller.ControllerImpl;
import yelpclassifier.controller.IController;
import yelpclassifier.controller.TestController;
import yelpclassifier.model.IModel;
import yelpclassifier.model.TestModel;
import yelpclassifier.view.IView;
import yelpclassifier.view.TestView;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the listening between the aspects of the MVC using a mock model, controller, and view.
 */
public class ListenersTest {
  private Appendable controllerAp;
  private Appendable modelAp;
  private IModel testModel;

  @BeforeEach
  public void init() {
    controllerAp = new StringBuilder();
    modelAp = new StringBuilder();
    testModel = new TestModel(modelAp);
  }

  @Test
  public void testSearchController() {
    Readable in = new StringReader("search");
    IView testView = new TestView(in);
    IController test = new TestController(testView, controllerAp);

    testView.start();
    assertEquals("Restaurant with name Name at location Location was searched for.",
            controllerAp.toString());
  }

  @Test
  public void testTrainController() {
    Readable in = new StringReader("train");
    IView testView = new TestView(in);
    IController test = new TestController(testView, controllerAp);

    testView.start();
    assertEquals("Restaurant with name Restaurant at location Location with classification " +
            "good was trained with.", controllerAp.toString());
  }

  @Test
  public void testQueryController() {
    Readable in = new StringReader("query");
    IView testView = new TestView(in);
    IController test = new TestController(testView, controllerAp);

    testView.start();
    assertEquals("Restaurant with name Restaurant at location Location was queried.",
            controllerAp.toString());
  }

  @Test
  public void testUpdateTrained() {
    Readable in = new StringReader("update");
    IView testView = new TestView(in);
    IController test = new TestController(testView, controllerAp);

    testView.start();
    assertEquals("Trained examples updated.",
            controllerAp.toString());
  }

  @Test
  public void testSaveController() {
    Readable in = new StringReader("save");
    IView testView = new TestView(in);
    IController test = new TestController(testView, controllerAp);

    testView.start();
    assertEquals("Saved at Filepath",
            controllerAp.toString());
  }

  @Test
  public void testLoadController() {
    Readable in = new StringReader("load");
    IView testView = new TestView(in);
    IController test = new TestController(testView, controllerAp);

    testView.start();
    assertEquals("Loaded from Filepath",
            controllerAp.toString());
  }

  @Test
  public void testShutdownController() {
    Readable in = new StringReader("shutdown");
    IView testView = new TestView(in);
    IController test = new TestController(testView, controllerAp);

    testView.start();
    assertEquals("Shutdown",
            controllerAp.toString());
  }

  @Test
  public void testSearchModel() {
    Readable in = new StringReader("search");
    IView testView = new TestView(in);
    IController test = new ControllerImpl(testModel, testView);

    testView.start();
    assertEquals("Model: Restaurant with name Name at location Location was searched for.",
            modelAp.toString());
  }

  @Test
  public void testTrainModel() {
    Readable in = new StringReader("train");
    IView testView = new TestView(in);
    IController test = new ControllerImpl(testModel, testView);

    testView.start();
    assertEquals("Model: Restaurant with name Restaurant at location Location with classification" +
                    " good was trained with.", modelAp.toString());
  }

  @Test
  public void testQueryModel() {
    Readable in = new StringReader("query");
    IView testView = new TestView(in);
    IController test = new ControllerImpl(testModel, testView);

    testView.start();
    assertEquals("Model: Restaurant with name Restaurant at location Location was queried.",
            modelAp.toString());
  }

  @Test
  public void testSaveModel() {
    Readable in = new StringReader("save");
    IView testView = new TestView(in);
    IController test = new ControllerImpl(testModel, testView);

    testView.start();
    assertEquals("Model: Saved at Filepath",
            modelAp.toString());
  }

  @Test
  public void testLoadModel() {
    Readable in = new StringReader("load");
    IView testView = new TestView(in);
    IController test = new ControllerImpl(testModel, testView);

    testView.start();
    assertEquals("Model: Loaded from Filepath",
            modelAp.toString());
  }

  @Test
  public void testShutdownModel() {
    Readable in = new StringReader("shutdown");
    IView testView = new TestView(in);
    IController test = new ControllerImpl(testModel, testView);

    testView.start();
    assertEquals("Model: Shutdown",
            modelAp.toString());
  }
}
