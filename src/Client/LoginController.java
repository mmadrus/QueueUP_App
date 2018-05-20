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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.DoubleAccumulator;

public class LoginController implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button queueButton, registerButton, forgotButton;

    private DataStream dataStream = new DataStream();
    private GUI GUI = new GUI();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setGuiDesign();

    }

    // Changes scene to register scene
    @FXML
    public void registerAccount(ActionEvent event) throws IOException {

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("regSample.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
    }

    // Changes to login screen
    @FXML
    public void login(ActionEvent event) throws IOException {

        //Check for textfields to be filled in
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {

            Alert accountError = new Alert(Alert.AlertType.INFORMATION);
            accountError.setTitle("Can not sign in");
            accountError.setHeaderText("No information to read");
            accountError.setContentText("Please enter your information in all of the textfields");
            accountError.show();

        } else { //If they aren't empty, send the username and password to the server

            // Pads the user name and password so that it can see if it matches anything in the db
            String username = String.format("%-16s", usernameField.getText()).replace(' ', '*');
            String password = String.format("%-20s", passwordField.getText()).replace(' ', '*');

            String user = "/6" + username + password;

            try {

                // Connects to the server and starts a thread
                dataStream.connectToServer();

                // Sends the username and password as a string with the command /6 to the server
                dataStream.sendDataStream("/k" + username);

                String serverResponse = dataStream.recieveDataStream();

                // Checks for the return statement from the server, if it returns true then the user will log into the chat
                if (serverResponse.equals("false")) {

                    dataStream.disconnectFromServer();

                    dataStream.connectToServer();
                    dataStream.sendDataStream(user);

                    String secondServerResponse = dataStream.recieveDataStream();

                    if (secondServerResponse.equals("true")) {

                        Data.getInstance().setUser(username);

                        Node node = (Node) event.getSource();
                        Stage stage = (Stage) node.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatSample.fxml"));
                        Parent root = loader.load();

                        Scene scene = new Scene(root, 1200, 700);
                        stage.setScene(scene);

                        dataStream.disconnectFromServer();

                    } else {

                        Alert accountError = new Alert(Alert.AlertType.INFORMATION);
                        accountError.setTitle("Wrong username or password");
                        accountError.setHeaderText("The username or password does not exist");
                        accountError.setContentText("Please enter your username and password");
                        accountError.show();

                        dataStream.disconnectFromServer();

                    }

                } else { //If the return statement from the the server is false then the username and password does not match something in the db

                    Alert accountError = new Alert(Alert.AlertType.INFORMATION);
                    accountError.setTitle("Banned!");
                    accountError.setHeaderText("You can not login because your banned.");
                    //accountError.setContentText("Please enter your username and password");
                    accountError.show();

                    dataStream.disconnectFromServer();
                }

            } catch (Exception e) {
                //If the server is down and the user is trying to log in this will pop up that the server is not running.
                Alert notConnected = new Alert(Alert.AlertType.INFORMATION);
                notConnected.setTitle("Unable to connect");
                notConnected.setHeaderText("Could not reach the server");
                notConnected.setContentText("Server is down");
                notConnected.show();
                e.printStackTrace();

                dataStream.disconnectFromServer();
            }

        }



    }

    @FXML
    public void handleForgotPassword (ActionEvent event) {


        try {

            dataStream.connectToServer();

            String username = String.format("%-16s", usernameField.getText()).replace(' ', '*');

            dataStream.sendDataStream("/f" + username);



        } catch (Exception e) {

            e.printStackTrace();
        }

        dataStream.disconnectFromServer();
    }

    private void setGuiDesign() {

        pane.setStyle("-fx-background-color: WHITE");
        imageView.setImage(GUI.setBackgroundImage());

        queueButton.setStyle(GUI.setButtonStyle());
        registerButton.setStyle(GUI.setButtonStyle());
        forgotButton.setStyle(GUI.setButtonStyle());

        usernameField.setStyle(GUI.setTextfieldStyle());
        passwordField.setStyle(GUI.setTextfieldStyle());

    }

}
