package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegController implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private TextField usernameField, emailField, confirmEmailField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    @FXML
    private Button cancelButton, registerButton;

    private DataStream dataStream = new DataStream();
    private GUI GUI = new GUI();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setGuiDesign();

    }

    // If the user wants to cancel his/her registration and then changes scene back to login screen
    @FXML
    public void cancel(ActionEvent event) throws IOException {

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginSample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);

    }

    // If user wants to finish his/her registration
    @FXML
    public void registerComplete(ActionEvent event) throws IOException {

        // Checks if password fields match each other and that the email fields match
        if (passwordField.getText().equals(confirmPasswordField.getText()) && emailField.getText().equals(
                confirmEmailField.getText())) {

            // Checks if the fields isnt empty, if they are; incomplete registration
            if (passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty() || emailField.getText().isEmpty()
                    || confirmEmailField.getText().isEmpty() || usernameField.getText().isEmpty()) {

                Alert accountError = new Alert(Alert.AlertType.INFORMATION);
                accountError.setTitle("Registration not complete!");
                accountError.setHeaderText("Enter information");
                accountError.setContentText("Please enter your information in all of the textfields");
                accountError.show();

            } else {

                // else, it checks so that the username is only using letters and numbers
                if (usernameLength(usernameField.getText())) {

                    //Pads the username and password to make it fit into the string thats being sent
                    String username = String.format("%-16s", usernameField.getText()).replace(' ', '*');
                    String password = String.format("%-20s", passwordField.getText()).replace(' ', '*');

                    //Create a string with command, the padded username and password with the email
                    String user = "/1" + username + password + emailField.getText();

                    //Connects to server
                    dataStream.connectToServer();

                    //Sends the user string to the server
                    dataStream.sendDataStream(user);

                    //If the return statement is true from the server then it changes scene to the login scene
                    if (dataStream.recieveDataStream().equals("true")) {

                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginSample.fxml"));
                        Parent root = loader.load();

                        Scene scene = new Scene(root, 1200, 700);
                        stage.setScene(scene);


                    } else { //If the return statement is false then the registration is not complete

                        Alert accountError = new Alert(Alert.AlertType.INFORMATION);
                        accountError.setTitle("Registration not complete!");
                        accountError.setHeaderText("Username or E-Mail already exists.");
                        accountError.show();
                    }

                    dataStream.disconnectFromServer();


                } else { //If the username is to long or there arent only letters and number
                    Alert accountError = new Alert(Alert.AlertType.INFORMATION);
                    accountError.setTitle("Username to short/long");
                    accountError.setHeaderText("Username has to be between 3-16 characters");
                    accountError.setContentText("Please enter username again");
                    accountError.show();
                }
            }


        } else { // If the password or email does not match each other

            Alert accountError = new Alert(Alert.AlertType.INFORMATION);
            accountError.setTitle("Registration not complete!");
            accountError.setHeaderText("Password/E-Mail has to match.");
            accountError.setContentText("Please enter the same password and e-mail!");
            accountError.show();
        }


    }

    // Method to check username is only letters and numbers
    private boolean usernameLength(String name) {
        return name.matches("[a-zA-Z0-9]{3,16}");
    }

    private void setGuiDesign () {

        pane.setStyle("-fx-background-color: WHITE");

        usernameField.setStyle(GUI.setTextfieldStyle());
        emailField.setStyle(GUI.setTextfieldStyle());
        confirmEmailField.setStyle(GUI.setTextfieldStyle());
        passwordField.setStyle(GUI.setTextfieldStyle());
        confirmPasswordField.setStyle(GUI.setTextfieldStyle());

        registerButton.setStyle(GUI.setButtonStyle());
        cancelButton.setStyle(GUI.setButtonStyle());

    }

}
