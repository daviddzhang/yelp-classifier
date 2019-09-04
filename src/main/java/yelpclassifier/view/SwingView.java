package yelpclassifier.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import yelpclassifier.controller.IViewFeatures;
import yelpclassifier.model.RestaurantClass;
import yelpclassifier.restaurant.IYelpRestaurant;

/**
 * This class is a view for the MVC pattern that utilizes Swing. It listens to low-level user
 * inputs through button presses and text fields, processes them into higher level events, and
 * sends them to the controller through its IViewFeatures field.
 */
public class SwingView extends JFrame implements IView, ActionListener {
  private List<IYelpRestaurant> searchRes = new ArrayList<>();
  private IViewFeatures features;

  private JPanel inputPanel;
  private JPanel searchResults;
  private JPanel trainedSoFar;

  private JButton searchButton;
  private JButton trainButton;
  private JButton queryButton;

  private JList<String> searchResultsList;
  private JList<String> categoryList;

  private JList<String> trainedSoFarList;

  private JTextField nameField;
  private JTextField locationField;

  ButtonGroup classSelections;

  /**
   * Constructs an instance of SwingView by initializing all the components.
   */
  public SwingView() {
    this.setLayout(new BorderLayout());
    this.setVisible(false);

    this.configureInputPanel();
    this.add(inputPanel, BorderLayout.WEST);

    this.configureSearchPanel();
    this.add(searchResults, BorderLayout.CENTER);

    this.configureTrainedPanel();
    this.add(trainedSoFar, BorderLayout.EAST);

    // add a closing method that properly ends the program
    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        features.shutdown();
        System.exit(0);
      }
    });

    this.pack();
  }

  /**
   * Configures the panel used to show the trained so far list.
   */
  private void configureTrainedPanel() {
    trainedSoFar = new JPanel(new BorderLayout());
    trainedSoFarList = new JList<>(new Vector<>());

    JScrollPane scrollList = new JScrollPane(trainedSoFarList);
    JLabel scrollListLabel = new JLabel("Trained so far");

    trainedSoFar.add(scrollListLabel, BorderLayout.NORTH);
    trainedSoFar.add(scrollList, BorderLayout.CENTER);
  }

  /**
   * Configures the panel displaying search results and user train/query inputs.
   */
  private void configureSearchPanel() {
    searchResults = new JPanel(new BorderLayout());

    JPanel listPanel = new JPanel(new BorderLayout());
    JLabel categoryLabel = new JLabel("Categories for selected restaurant:");
    categoryList = new JList<>(new Vector<>());
    categoryList.setVisibleRowCount(3);
    categoryList.setFixedCellWidth(searchResults.getWidth());
    JScrollPane scrollCategories = new JScrollPane(categoryList);

    searchResultsList = new JList<>(new Vector<>());
    searchResultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    searchResultsList.setVisibleRowCount(10);
    searchResultsList.addListSelectionListener((e -> {
      if (searchResultsList.getSelectedIndex() == -1) {
        return;
      }

      IYelpRestaurant selected = this.searchRes.get(searchResultsList.getSelectedIndex());
      Vector<String> categories = new Vector<>(selected.getCategories());
      categoryList.setListData(categories);
      categoryList.setSelectedIndex(0);
    }));

    JScrollPane scrollSearchList = new JScrollPane(searchResultsList);
    JLabel scrollListLabel = new JLabel("Search Results");

    listPanel.add(scrollSearchList, BorderLayout.NORTH);
    listPanel.add(categoryLabel, BorderLayout.CENTER);
    listPanel.add(scrollCategories, BorderLayout.SOUTH);

    JPanel radioButtonPanel = new JPanel(new FlowLayout());
    classSelections = new ButtonGroup();
    JRadioButton good = new JRadioButton("Good");
    good.setActionCommand("good");

    JRadioButton okay = new JRadioButton("Okay");
    okay.setActionCommand("okay");

    JRadioButton bad = new JRadioButton("Bad");
    bad.setActionCommand("bad");

    classSelections.add(good);
    classSelections.add(okay);
    classSelections.add(bad);

    radioButtonPanel.add(good);
    radioButtonPanel.add(okay);
    radioButtonPanel.add(bad);

    JPanel buttonPanel = new JPanel(new FlowLayout());
    trainButton = new JButton("Train");
    trainButton.setActionCommand("train");
    trainButton.addActionListener(this);

    queryButton = new JButton("Query");
    queryButton.setActionCommand("query");
    queryButton.addActionListener(this);

    buttonPanel.add(trainButton);
    buttonPanel.add(queryButton);

    JPanel allButtons = new JPanel(new BorderLayout());
    allButtons.add(radioButtonPanel, BorderLayout.NORTH);
    allButtons.add(buttonPanel, BorderLayout.CENTER);

    searchResults.add(scrollListLabel, BorderLayout.NORTH);
    searchResults.add(listPanel, BorderLayout.CENTER);
    searchResults.add(allButtons, BorderLayout.SOUTH);
  }

  /**
   * Configures the user input search components.
   */
  private void configureInputPanel() {
    inputPanel = new JPanel(new BorderLayout());

    JPanel searchInputPanel = new JPanel();

    JPanel namePanel = new JPanel(new FlowLayout());
    JLabel nameLabel = new JLabel("Name or Keyword: ");
    nameField = new JTextField(10);
    JScrollPane nameScroll = new JScrollPane(nameField);

    namePanel.add(nameLabel);
    namePanel.add(nameScroll);

    JPanel locPanel = new JPanel(new FlowLayout());
    JLabel locationLabel = new JLabel("Location (City, State/Country): ");
    locationField = new JTextField(15);
    JScrollPane locScroll = new JScrollPane(locationField);

    locPanel.add(locationLabel);
    locPanel.add(locScroll);

    JPanel searchPanel = new JPanel();

    searchButton = new JButton("Search");
    searchButton.setActionCommand("search");
    searchButton.addActionListener(this);

    searchPanel.add(searchButton);

    searchInputPanel.add(namePanel, BorderLayout.NORTH);
    searchInputPanel.add(locPanel, BorderLayout.CENTER);
    searchInputPanel.add(searchPanel, BorderLayout.SOUTH);

    JPanel ioPanel = new JPanel();

    JButton saveButton = new JButton("Save");
    saveButton.setActionCommand("save");
    saveButton.addActionListener(this);

    JButton loadButton = new JButton("Load");
    loadButton.setActionCommand("load");
    loadButton.addActionListener(this);

    ioPanel.add(saveButton);
    ioPanel.add(loadButton);

    inputPanel.add(searchInputPanel, BorderLayout.NORTH);
    inputPanel.add(ioPanel, BorderLayout.SOUTH);

  }

  @Override
  public void start() {
    this.setVisible(true);
  }

  @Override
  public void setFeatures(IViewFeatures features) {
    this.features = features;
  }

  @Override
  public void setSearchResults(List<IYelpRestaurant> results) {
    this.searchRes = results;
    if (results.size() == 0) {
      this.errorPopup("Search returned no restaurants. Please try again.");
      return;
    }

    Vector<String> newList = new Vector<>();
    for (IYelpRestaurant restaurant : results) {
      String formatted = restaurant.getName() + " | " + restaurant.getLocation();
      newList.add(formatted);
    }

    this.searchResultsList.setListData(newList);
  }

  @Override
  public void setTrainedSoFar(Map<IYelpRestaurant, RestaurantClass> trained) {
    Vector<String> trainedModel = new Vector<>();
    for (Map.Entry<IYelpRestaurant, RestaurantClass> entry : trained.entrySet()) {
      IYelpRestaurant currentRest = entry.getKey();
      String formatted =
              currentRest.getName() + " | " + currentRest.getLocation() + " : "
                      + RestaurantClass.enumToString(entry.getValue());
      trainedModel.add(formatted);
    }

    trainedSoFarList.setListData(trainedModel);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "search":
        this.search();
        break;
      case "train":
        this.train();
        break;
      case "query":
        this.query();
        break;
      case "save":
        this.save();
        break;
      case "load":
        this.load();
        break;
      default:
    }
  }

  /**
   * Performs the operations to load a previous state of the application.
   */
  private void load() {
    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    jfc.setDialogTitle("Choose a file to load: ");
    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

    int returnValue = jfc.showOpenDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      if (jfc.getSelectedFile().isFile()) {
        this.features.load(jfc.getSelectedFile().getPath());
        this.features.updateTrained();
      }
      else {
        this.errorPopup("Please select a file to load from.");
      }
    }
  }

  /**
   * Performs the operations to save the current state of the application.
   */
  private void save() {
    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    jfc.setDialogTitle("Choose a directory to save your file: ");
    jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    int returnValue = jfc.showSaveDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      if (jfc.getSelectedFile().isDirectory()) {
        this.features.save(jfc.getSelectedFile() + "/save_data.ser");
      }
      else {
        this.errorPopup("Please select a directory to save to.");
      }
    }
  }

  /**
   * Performs the operations to query the model with the selected restaurant.
   */
  private void query() {
    if (searchResultsList.isSelectionEmpty()) {
      return;
    }

    try {
      IYelpRestaurant selected = this.searchRes.get(searchResultsList.getSelectedIndex());
      String category = categoryList.getSelectedValue();
      String message = "Predicted opinion on selected restaurant: " +
              RestaurantClass.enumToString(this.features.queryRestaurant(selected, category));
      JOptionPane.showMessageDialog(new JFrame(), message, "Result", JOptionPane.PLAIN_MESSAGE);
    } catch (Exception e) {
      this.errorPopup(e.getMessage());
    }
  }

  /**
   * Performs the operations to train the model with the selected restaurant.
   */
  private void train() {
    if (searchResultsList.isSelectionEmpty()) {
      return;
    }

    if (classSelections.getSelection() == null) {
      this.errorPopup("Please select whether the restaurant was good, okay, or bad.");
      return;
    }

    IYelpRestaurant selected = this.searchRes.get(searchResultsList.getSelectedIndex());
    RestaurantClass selectedClass =
            RestaurantClass.stringToEnum(classSelections.getSelection().getActionCommand());
    String category = categoryList.getSelectedValue();

    try {
      this.features.trainRestaurant(selected, category, selectedClass);
      this.features.updateTrained();
    } catch (Exception e) {
      this.errorPopup(e.getMessage());
    }
  }

  /**
   * Performs the operations to search/query the Yelp API with the supplied inputs.
   */
  private void search() {
    if (nameField.getText().equals("")) {
      this.errorPopup("Please enter a name or keyword before searching.");
      return;
    }

    if (locationField.getText().equals("")) {
      this.errorPopup("Please enter a location before searching.");
      return;
    }

    String name = nameField.getText();
    String location = locationField.getText();

    this.features.search(name, location);
  }

  /**
   * Pop up an error message with the given message.
   *
   * @param errorMessage the message to display
   */
  private void errorPopup(String errorMessage) {
    JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
  }
}
