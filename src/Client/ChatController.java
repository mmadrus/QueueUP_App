package Client;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.security.Guard;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField messageField;
    @FXML
    private TextArea textArea, messageArea, helpMessageArea, offlineUsersArea, onlineUsersArea;
    @FXML
    private Button sendButton, logoutButton;
    @FXML
    private ImageView chatBackground, chatBackground1, settingImageButton;

    private String currentUser;

    private DataStream dataStream = new DataStream();
    private GUI GUI = new GUI();
    private User userClass = new User();



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setGuiDesign();

        // Connects to the server when the scene is initialized
        dataStream.connectToServer();

        //dataStream.sendDataStream("/u" + getCurrentUser());
        userClass.userList.add("Not really a user");

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

    public String getCurrentUser() {
        return currentUser;
    }

    public void addTab () {

        tabPane.getTabs().add(GUI.createNewTab());

    }

    // Method to update chat
    private void updateChat() {

        try {
            Thread thread = new Thread(() -> {

                while (true) {

                    try {

                        dataStream.sendDataStream("/a1");

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
                                // saves them into the StringBuilder finalUser
                                if (!String.valueOf(user.charAt(p)).equals("*")) {

                                    finalUser.append(String.valueOf(user.charAt(p)));
                                }

                            }

                            // Appends the text the finaluser and message into the message area for the chat
                            messageArea.appendText("[" + finalUser + "] " + msg.substring(20) + "\n");


                        } else if (msg.substring(0,2).equals("/u")) {

                            //Saves the user name from the string into a variable
                            String user = msg.substring(2);

                            // Create a new string builder to later save the user name in
                            StringBuilder finalUser = new StringBuilder();

                            // For loop to convert the padded username returned from the server into a username without pads
                            for (int p = 0; p < user.length(); p++) {

                                // Removes the * form the returned username, keeps the letters and numbers and then
                                // saves them into the StringBuilder finalUser
                                if (!String.valueOf(user.charAt(p)).equals("*")) {

                                    finalUser.append(String.valueOf(user.charAt(p)));
                                }

                            }

                            // Appends the text the finaluser and message into the message area for the chat
                            onlineUsersArea.appendText(finalUser + "\n");

                        } else if (msg.substring(0,2).equals("/a")) {

                            //Saves the user name from the string into a variable
                            String user = msg.substring(2, 18);

                            // Create a new string builder to later save the user name in
                            StringBuilder finalUser = new StringBuilder();

                            // For loop to convert the padded username returned from the server into a username without pads
                            for (int p = 0; p < user.length(); p++) {

                                // Removes the * form the returned username, keeps the letters and numbers and then
                                // saves them into the StringBuilder finalUser
                                if (!String.valueOf(user.charAt(p)).equals("*")) {

                                    finalUser.append(String.valueOf(user.charAt(p)));
                                }


                            }

                            boolean exist = false;

                            for (int i = 0; i < userClass.userList.size(); i++){

                                if (String.valueOf(finalUser).equals(userClass.userList.get(i))) {


                                    exist = true;
                                    break;

                                } else {

                                    exist = false;


                                }
                            }

                            if (exist == false) {

                                userClass.userList.add(String.valueOf(finalUser));
                                onlineUsersArea.appendText(String.valueOf(finalUser)+"\n");

                            }


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

    private void setGuiDesign () {

        pane.setStyle("-fx-background-color: WHITE");
        tabPane.setStyle("-fx-background-color: WHITE");

        chatBackground.setImage(GUI.setBackgroundImage());
        chatBackground1.setImage(GUI.setBackgroundImage());

        messageField.setStyle("-fx-background-color: WHITE; -fx-background-radius: 16px;");

        sendButton.setStyle(GUI.setButtonStyle());
        logoutButton.setStyle(GUI.setButtonStyle());

        settingImageButton.setImage(GUI.setSettingImage());

    }

    public void rotateSettingForward () {

        try {

            RotateTransition rt = new RotateTransition();
            rt.setNode(settingImageButton);
            rt.setFromAngle(0);
            rt.setToAngle(45);
            rt.setInterpolator(Interpolator.LINEAR);
            rt.setCycleCount(1);
            rt.setDuration(new Duration(300));

            rt.play();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void rotateSettingBackward () {

        try {

            RotateTransition rt = new RotateTransition();
            rt.setNode(settingImageButton);
            rt.setFromAngle(0);
            rt.setToAngle(-45);
            rt.setInterpolator(Interpolator.LINEAR);
            rt.setCycleCount(1);
            rt.setDuration(new Duration(300));

            rt.play();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
