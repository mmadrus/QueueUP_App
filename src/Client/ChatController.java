package Client;

import Server.Database;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.security.Guard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private TextField searchField;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton, logoutButton;
    @FXML
    private ImageView chatBackground, chatBackground1, settingImageButton;

    @FXML
    private ListView<String> onlineUsersArea;

    @FXML
    private Tab currentTab;

    private String currentUser;

    private DataStream dataStream = new DataStream();
    private GUI GUI = new GUI();
    private User userClass = new User();

    //private Database database;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setGuiDesign();

        // Connects to the server when the scene is initialized
        dataStream.connectToServer();

        //dataStream.sendDataStream("/u" + getCurrentUser());
        userClass.userList.add("Not really a user");

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->currentTab =newValue);


        // Creates a thread that constantly updates the chat
        updateChat();

        // Ongoing method for searching for online users.
        /*searchField.setOnKeyTyped(event -> {
            searchField.requestFocus();
            String searchValue = searchField.getText();

            User user = new User();
            ArrayList<String>onlineUser=user.getUserList();

            Collections.sort(onlineUser);
            for(int i =0;i<onlineUser.size();i++){
                String s=onlineUser.get(i);

                if(s.substring(0,1).equalsIgnoreCase(searchValue.substring(0,1))){
                 for (int j=0;j<onlineUser.size();j++){
                  ObservableList<String>hej = FXCollections.observableArrayList();
                  String sj=onlineUser.get(j);
                  hej.add(sj);

                    onlineUsersArea.setItems(hej);
                 }
                }
                else{
                    System.out.println("Failure");
                }

            }




        });*/



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

            // Sends message to selected tab, needs to change to send to tab with that ID
            TextArea thisArea = new TextArea();
            thisArea.getUserData();
            AnchorPane pane = ((AnchorPane) tabPane.getSelectionModel().getSelectedItem().getContent());
            thisArea =(TextArea)pane.getChildren().get(1);

            System.out.println("Send to TextArea: " + thisArea.getUserData());

            // Creates a string with the message command, current user and the message, then sends it to the server
            dataStream.sendDataStream("/m" + thisArea.getUserData() + currentUser + messageField.getText());

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

    public void addTab (String user, String id) {

        tabPane.getTabs().add(GUI.createNewTab(user, id));

    }

    @FXML
    public void popupMenu(MouseEvent event) {
        MenuItem sendWhisper = new MenuItem("Send Message");
        MenuItem closeMenu = new MenuItem("Close");
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(sendWhisper, closeMenu);
        contextMenu.setAutoHide(true);
        contextMenu.show(pane, event.getScreenX(), event.getScreenY());
        closeMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                contextMenu.hide();
            }
        });

        sendWhisper.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String wantedUser = onlineUsersArea.getSelectionModel().getSelectedItem();
                String wantedUsername = String.format("%-16s", wantedUser).replace(' ', '*');
                dataStream.sendDataStream("/w" + currentUser + wantedUsername);

            }
        });

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
                            String user = msg.substring(12,28);

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

                            System.out.println("/m test: " + finalUser);
                            System.out.println("/m room: " + msg.substring(2,10));

                            for (Tab room: GUI.getTabHandler()) {

                                System.out.println("hejsan");
                                System.out.println(room);
                                System.out.println(msg.substring(2,12));

                                if (room.getUserData().equals(msg.substring(2,12))) {

                                    System.out.println("bajs");


                                    // Sends message to selected tab, needs to change to send to tab with that ID
                                    TextArea thisArea = new TextArea();
                                    //thisArea.getUserData();
                                    AnchorPane pane = ((AnchorPane) room.getContent());

                                    //TextArea pool = ((TextArea) lol.getChildren().get(1));
                                    thisArea =(TextArea)pane.getChildren().get(1);

                                    // Appends the text the finaluser and message into the message area for the chat
                                    thisArea.appendText("[" + finalUser + "] " + msg.substring(28) + "\n");
                                    System.out.println("Receiving TextArea: " + thisArea.getUserData());

                                    break;



                                }
                            }


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
                            //onlineUsersArea.appendText(finalUser + "\n");

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
                                onlineUsersArea.getItems().add(String.valueOf(finalUser));


                            }


                        } else if (msg.substring(0,2).equals("/w")) {

                            System.out.println("This message: " + msg);

                            //Saves the user name from the string into a variable
                            String user = msg.substring(2, 18);

                            System.out.println("user 1:" + user);

                            // Create a new string builder to later save the user name in
                            StringBuilder finalUser1 = new StringBuilder();

                            // For loop to convert the padded username returned from the server into a username without pads
                            for (int p = 0; p < user.length(); p++) {

                                // Removes the * form the returned username, keeps the letters and numbers and then
                                // saves them into the StringBuilder finalUser
                                if (!String.valueOf(user.charAt(p)).equals("*")) {

                                    finalUser1.append(String.valueOf(user.charAt(p)));
                                }


                            }

                            System.out.println("tester 1: " + finalUser1);

                            //Saves the user name from the string into a variable
                            String userTwo = msg.substring(18, 34);

                            System.out.println("user 2:" + userTwo);

                            // Create a new string builder to later save the user name in
                            StringBuilder finalUser2 = new StringBuilder();

                            // For loop to convert the padded username returned from the server into a username without pads
                            for (int p = 0; p < userTwo.length(); p++) {

                                // Removes the * form the returned username, keeps the letters and numbers and then
                                // saves them into the StringBuilder finalUser
                                if (!String.valueOf(userTwo.charAt(p)).equals("*")) {

                                    finalUser2.append(String.valueOf(userTwo.charAt(p)));
                                }


                            }

                            System.out.println("tester 2: " + finalUser2);


                            if (msg.substring(2,18).equals(currentUser) || msg.substring(18,34).equals(currentUser)) {

                                String tabId = msg.substring(34);

                                if (user.equals(currentUser)) {

                                    Platform.runLater(new Runnable(){
                                        @Override
                                        public void run() {

                                            addTab(String.valueOf(finalUser2),tabId);
                                        }
                                    });

                                } else {

                                    Platform.runLater(new Runnable(){
                                        @Override
                                        public void run() {

                                            addTab(String.valueOf(finalUser1),tabId);
                                        }
                                    });

                                }


                            }


                            System.out.println(finalUser1 + "\n" + finalUser2);
                            System.out.println(msg.substring(34));



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
