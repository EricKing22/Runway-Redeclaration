package uk.ac.soton.comp2211.runwayredeclaration.Component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import uk.ac.soton.comp2211.runwayredeclaration.Calculator.PasswordChecker;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LogInVBox extends VBox {
  private User user;

  private PasswordField passwordField;
  private TextField usernameTextField;
  private TextField visiblePasswordField;
  private Button showHidePasswordButton;
  private Button logInSuccessful;

  private Map<String, User> users = new HashMap<>();


  public LogInVBox(Button logInSuccessful) {
    super();
    this.logInSuccessful = logInSuccessful;
    this.setMaxHeight(450);
    this.setMaxWidth(600);
    this.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));

    build();
    this.setAlignment(Pos.CENTER);
    this.user = new User("Guest", "", "Guest");

  }
  /**
   * Initialise this scene. Called after creation
   */
  public void initialise(){};

  /**
   * Build the layout of the scene
   */
  public void build(){
    loadUsers();

    Label usernameLabel = new Label("Enter your Username:");
    Label passwordLabel = new Label("Enter your Password:");

    usernameTextField = new TextField();
    usernameTextField.setPromptText("Username");

    passwordField = new PasswordField();
    passwordField.setPromptText("Password");
    visiblePasswordField = new TextField();
    visiblePasswordField.setManaged(false);
    visiblePasswordField.setVisible(false);

    passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

    showHidePasswordButton = new Button("Show");
    showHidePasswordButton.setOnAction(e -> togglePasswordVisibility());

    Button loginButton = new Button("Log In");
    loginButton.setOnAction(e -> handleLogin());
    loginButton.getStyleClass().add("button-login");
    HBox boxForButton = new HBox(loginButton);
    boxForButton.setAlignment(Pos.CENTER);

    Hyperlink registerLink = new Hyperlink("here");
    registerLink.setOnAction(e -> handleRegister());

    Button guestButton = new Button("Continue as Guest");
    guestButton.setOnAction(e -> handleGuestLogin());
    guestButton.getStyleClass().add("button-guest");
    HBox boxForGuest = new HBox(guestButton);
    boxForGuest.setAlignment(Pos.CENTER);


    HBox userNameFields = new HBox(10, usernameLabel, usernameTextField);
    userNameFields.setAlignment(Pos.CENTER);
    HBox passwordFields = new HBox(10, passwordLabel, passwordField, visiblePasswordField, showHidePasswordButton);
    passwordFields.setAlignment(Pos.CENTER);

    VBox layout = new VBox(10); // 10 is the spacing between elements

    HBox noAccountBox = new HBox( new Label("Don't have an account? Register"),registerLink);
    noAccountBox.setAlignment(Pos.CENTER);

    VBox logInStuff = new VBox(15,userNameFields, passwordFields, boxForButton );
    ImageView planeIconView = new ImageView(new Image(getClass().getResourceAsStream("/images/planeIcon.png")));
    planeIconView.setFitHeight(150);
    planeIconView.setFitWidth(150);
    HBox plane = new HBox(planeIconView);
    plane.setAlignment(Pos.CENTER);
    VBox everything = new VBox(20,plane, logInStuff,boxForGuest, noAccountBox);

    layout.getChildren().addAll(everything);

    this.getChildren().add(layout);
  }

  private void togglePasswordVisibility() {
    if (passwordField.isVisible()) {
      passwordField.setManaged(false);
      passwordField.setVisible(false);
      visiblePasswordField.setManaged(true);
      visiblePasswordField.setVisible(true);
      showHidePasswordButton.setText("Hide");
    } else {
      passwordField.setManaged(true);
      passwordField.setVisible(true);
      visiblePasswordField.setManaged(false);
      visiblePasswordField.setVisible(false);
      showHidePasswordButton.setText("Show");
    }
  }

  private void loadUsers() {

    try {
      InputStream inputStream = getClass().getResourceAsStream("/predefined/Users.csv");
      if (inputStream == null) {
        throw new IOException("Resource not found");
      }
      try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
        String line;
        while ((line = br.readLine()) != null) {
          System.out.println(line);
          String[] parts = line.split(",");
          if (parts.length == 3) {
            users.put(parts[0], new User(parts[0], parts[1], parts[2]));
          }
          else{
            // Invalid file format
            throw new IOException("Invalid file format");
          }
        }
      }
    } catch (IOException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Failed to Load User Data");
      alert.setContentText("The application could not parse the user data file: " + e.getMessage());
      alert.showAndWait();
    }
  }

  private void handleLogin() {
    User user = users.get(usernameTextField.getText());
    if (user != null && user.password.equals(passwordField.getText())) {
      System.out.println("Login successful for " + user.username + " with permission: " + user.permissionLevel);
      this.user = user;
      logInSuccessful.fire();
    } else {
      usernameTextField.clear();
      passwordField.clear();
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText("Incorrect username or password");
      alert.showAndWait();
    }
  }



  private void handleRegister() {
    System.out.println("Register link clicked");
    this.getChildren().clear();
    Label usernameLabel = new Label("Create your username:");
    Label passwordLabel = new Label("Enter your Password:");

    TextField newUsernameTextField = new TextField();
    usernameTextField.setPromptText("Username");

    TextField newPasswordField = new TextField();
    newPasswordField.setPromptText("Password");
    PasswordChecker passwordChecker = new PasswordChecker();
    passwordChecker.check(newPasswordField.getText());

    ImageView planeIconView = new ImageView(new Image(getClass().getResourceAsStream("/images/planeIcon.png")));
    planeIconView.setFitHeight(150);
    planeIconView.setFitWidth(150);
    HBox plane = new HBox(planeIconView);


    HBox usname = new HBox(5, usernameLabel, newUsernameTextField);
    Label checker = new Label(passwordChecker.check(newPasswordField.getText()));

    newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
      checker.setText(passwordChecker.check(newValue));
    });
    HBox pwname = new HBox(5, passwordLabel, newPasswordField, checker);



    plane.setAlignment(Pos.CENTER);
    usname.setAlignment(Pos.CENTER);
    pwname.setAlignment(Pos.CENTER);

    ChoiceBox<String> roleChoiceBox = new ChoiceBox<>();
    roleChoiceBox.getItems().addAll("Admin", "User", "Guest");
    roleChoiceBox.setValue("Select your role"); // Default text

    roleChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
      if (newValue != null) {
        System.out.println("Selected role: " + newValue);
      }
    });

    HBox roleBox = new HBox(5, roleChoiceBox);
    roleBox.setAlignment(Pos.CENTER);

    // Continue as guest button
    Button createAccount = new Button("Create Account");
    createAccount.setAlignment(Pos.CENTER);
    createAccount.setOnAction(e -> {
      try {
        handleCreatingAccount(newUsernameTextField.getText(), newPasswordField.getText(), roleChoiceBox.getValue());
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
      newPasswordField.clear();
      newUsernameTextField.clear();
    });
    createAccount.getStyleClass().add("button-login");
    HBox boxForCreateAccount = new HBox(createAccount);
    boxForCreateAccount.setAlignment(Pos.CENTER);

    VBox register = new VBox(10,plane, usname, pwname, roleBox, boxForCreateAccount);
    this.getChildren().add(register);
  }



  private void handleCreatingAccount(String newUsername, String newPassword, String role) throws Exception{
    if (users.containsKey(newUsername)) {
      throw new Exception("Username already exists.");
    }
    if (newUsername == null || newUsername.isEmpty()){
      throw new Exception("Username cannot be empty.");

    }
    if (newPassword == null || newPassword.isEmpty()) {
      throw new Exception("Password cannot be empty.");
    }

    appendToCSV(newUsername,newPassword,role);

    user = new User(newUsername, newPassword, role);
    users.put(newUsername, user);
    logInSuccessful.fire();
  }


  private void appendToCSV(String username, String password, String role) {


    String newUserLine = "\n" + username + "," + password + "," + role ;
    URL csvFilePath = getClass().getResource("/predefined/Users.csv");


    try {
      File usersFile = new File(csvFilePath.toURI());
      FileWriter fw = new FileWriter(usersFile,true);
      fw.write(newUserLine);
      fw.close();
      System.out.println("User added to file");
    } catch (Exception e) {
      System.out.println("Error writing to file: " + e.getMessage());
    }
  }


  private void handleGuestLogin() {
    System.out.println("Guest login button clicked");
    logInSuccessful.fire();
    user.setPermissionLevel("Guest");

  }




  public User getUser() {
    return user;
  }




}
