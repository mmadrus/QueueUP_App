package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private DataStream dataStream = new DataStream();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setStyle("-fx-background-color: WHITE");
        File file = new File("resources/icon.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);


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

                // Connects to the server and starts a thread
                dataStream.connectToServer();

                // Sends the username and password as a string with the command /6 to the server
                dataStream.sendDataStream(user);

                // Checks for the return statement from the server, if it returns true then the user will log into the chat
                if (dataStream.recieveDataStream().equals("true")) {

                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("chatSample.fxml"));
                Parent root = loader.load();

                // Calls for the chat controller
                ChatController chatController = loader.getController();
                // Sets the current user in the chat controller to the one filled in into the username textfield
                chatController.setCurrentUser(username);

                Scene scene = new Scene(root, 1200, 700);
                stage.setScene(scene);

                } else { //If the return statement from the the server is false then the username and password does not match something in the db

                    Alert accountError = new Alert(Alert.AlertType.INFORMATION);
                    accountError.setTitle("Wrong username or password");
                    accountError.setHeaderText("The username or password does not exist");
                    accountError.setContentText("Please enter your username and password");
                    accountError.show();

                }
        }

        //The thread closes
        dataStream.disconnectFromServer();

    }

}
