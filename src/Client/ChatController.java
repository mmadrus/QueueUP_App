package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    DataStream dataStream = new DataStream();
    @FXML
    private AnchorPane pane;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField messageField;
    @FXML
    private TextArea messageArea;
    private TextArea helpMessageArea;
    private String currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setStyle("-fx-background-color: WHITE");
        tabPane.setStyle("-fx-background-color: WHITE");

        // Connects to the server when the scene is initialized
        dataStream.connectToServer();

        // Creates a thread that constantly updates the chat
        updateChat();


    }

    // If the user wants to log out
    @FXML
    public void logout(ActionEvent event) throws IOException {

        // Disconnects from the server
        dataStream.disconnectFromServer();
        //Sets current user to null
        setCurrentUser(null);

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginSample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
    }

    // If the user sends a message
    @FXML
    public void send(ActionEvent event) {

        // Checks that the textfield for the message isnt empty
        if (!messageField.getText().isEmpty()) {

            // Creates a string with the message command, current user and the message, then sends it to the server
            dataStream.sendDataStream("/m" + "[" + currentUser + "] " + messageField.getText());

            messageField.clear();

        }


    }

    // Method to set current user
    public String setCurrentUser(String user) {

        currentUser = user;

        return user;
    }

    // Method to update chat
    public void updateChat() {

        try {
            Thread thread = new Thread(() -> {

                while (true) {

                    try {

                        // Saves the data stream from the server into a string
                        String msg = dataStream.recieveDataStream();

                        // Checks for the command at the first two indexes

                        // If the command is /m then it is a message
                        if (msg.substring(0,2).equals("/m")) {

                            //Saves the user name from the string into a variable
                            String user = msg.substring(3,19);

                            // Create a new string builder to later save the user name in
                            StringBuilder finalUser = new StringBuilder();

                            // For loop to convert the padded username returned from the server into a username without pads
                            for (int p = 0; p < user.length(); p++) {

                                // Removes the * form the returned username, keeps the letters and numbers and then
                                // saves them into the StringBuilder finaluser
                                if (!String.valueOf(user.charAt(p)).equals("*")) {

                                    finalUser.append(String.valueOf(user.charAt(p)));
                                }

                            }

                            // Appends the text the finaluser and message into the message area for the chat
                            messageArea.appendText("[" + finalUser + "] " + msg.substring(20) + "\n");

                        }


                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

            });

            thread.start();

        } catch (NullPointerException nPE) {

            nPE.getSuppressed();

        }
    }

}
