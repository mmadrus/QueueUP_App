package Client;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

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
    private ImageView chatBackground1, settingImageButton;
    @FXML
    private ListView<String> onlineUsersArea;
    @FXML
    private Tab currentTab;

    private volatile boolean running = true;

    private DataStream dataStream = new DataStream();
    private GUI GUI = new GUI();
    private User userClass;
    private ArrayList<String> userList = new ArrayList<>();
    private Sound sound = new Sound();
    private Admin admin = new Admin();
    //public String currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setGuiDesign();
        addGeneralTab();

        // Connects to the server when the scene is initialized
        dataStream.connectToServer();

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> currentTab = newValue);
        admin.addAdmin();

        // Creates a thread that constantly updates the chat
        updateChat();

    }

    // If the user wants to log out
    @FXML
    public void handleLogoutButton(ActionEvent event) throws IOException, InterruptedException {

        dataStream.sendDataStream("/0" + dataStream.getSocketPort() + Data.getInstance().getUser());

        running = false;
        userList.clear();
        GUI.emptyTabHandler();
        //Sets current user to null
        //setCurrentUser(null);
        userClass = new User(null);

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginSample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);

        dataStream.disconnectFromServer();
    }

    @FXML
    public void handleSettingsButton(MouseEvent event){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {

                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("settingSample.fxml"));

                    //Data data = Data.getInstance();
                    //data.setUser(userClass.getCurrentUser());

                    stage.setTitle("Settings");
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


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
            thisArea = (TextArea) pane.getChildren().get(1);

            System.out.println(thisArea.getUserData());

            // Creates a string with the message command, current user and the message, then sends it to the server
            dataStream.sendDataStream("/m" + thisArea.getUserData() + Data.getInstance().getUser() + messageField.getText());

            messageField.clear();

        }


    }

    // Method to set current user
    /*public void setCurrentUser(String user) {

        userClass.setCurrentUser(user);
        currentUser = userClass.getCurrentUser();
    }

    public String getCurrentUser() {
        return currentUser;
    }*/

    public void addTab(String user, String id) {

        tabPane.getTabs().add(GUI.createNewTab(user, id));

    }

    @FXML
    public void popupMenu(MouseEvent event) {
        MenuItem sendWhisper = new MenuItem("Send Message");
        MenuItem ban = new MenuItem("Ban user");
        MenuItem closeMenu = new MenuItem("Close");


        boolean isAdmin = false;

        for (int i = 0; i < admin.getAdminList().size(); i++) {

            if (Data.getInstance().getUser().equals(admin.getAdminList().get(i))) {

                isAdmin = true;
                break;

            } else {

                isAdmin = false;
            }

        }

        System.out.println(isAdmin);

            if (isAdmin == false) {
                ContextMenu contextMenuUser = new ContextMenu();
                contextMenuUser.getItems().addAll(sendWhisper, closeMenu);
                contextMenuUser.setAutoHide(true);
                contextMenuUser.show(pane, event.getScreenX(), event.getScreenY());

                closeMenu.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        contextMenuUser.hide();
                    }
                });

                sendWhisper.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String wantedUser = onlineUsersArea.getSelectionModel().getSelectedItem();

                        try {

                            if (!wantedUser.equals(null)) {
                                String wantedUsername = String.format("%-16s", wantedUser).replace(' ', '*');
                                dataStream.sendDataStream("/w" + Data.getInstance().getUser() + wantedUsername);
                            }

                        } catch (NullPointerException e) {

                            contextMenuUser.hide();
                        }

                    }
                });
            } else if (isAdmin == true) {

                ContextMenu contextMenuAdmin = new ContextMenu();
                contextMenuAdmin.getItems().addAll(sendWhisper, ban, closeMenu);
                contextMenuAdmin.setAutoHide(true);
                contextMenuAdmin.show(pane, event.getScreenX(), event.getScreenY());

                closeMenu.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        contextMenuAdmin.hide();
                    }
                });

                sendWhisper.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String wantedUser = onlineUsersArea.getSelectionModel().getSelectedItem();

                        try {

                            if (!wantedUser.equals(null)) {
                                String wantedUsername = String.format("%-16s", wantedUser).replace(' ', '*');
                                dataStream.sendDataStream("/w" + Data.getInstance().getUser() + wantedUsername);
                            }

                        } catch (NullPointerException e) {

                            contextMenuAdmin.hide();
                        }

                    }
                });

                ban.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        String wantedUser = onlineUsersArea.getSelectionModel().getSelectedItem();
                        String paddedUser = String.format("%-16s", wantedUser).replace(' ', '*');
                        dataStream.sendDataStream("/b" + paddedUser);
                    }
                });

            }

    }

    // Method to update chat
    private void updateChat() {

        try {
            Thread thread = new Thread(() -> {

                while (running) {

                    try {

                        dataStream.sendDataStream("/a1");

                        // Saves the data stream from the server into a string
                        String msg = dataStream.recieveDataStream();

                        // Checks for the command at the first two indexes

                        // If the command is /m then it is a message
                        if (msg.substring(0, 2).equals("/m")) {

                            System.out.println(msg);
                            //Saves the user name from the string into a variable
                            String user = msg.substring(12, 28);

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

                            for (Tab room : GUI.getTabHandler()) {


                                if (room.getUserData().equals(msg.substring(2, 12))) {

                                    boolean roomOpen = false;

                                    for (int i = 1; i < GUI.getTabHandler().size(); i++) {

                                        if (!tabPane.getTabs().get(i - 1).equals(room)) {

                                            roomOpen = false;

                                        } else {

                                            roomOpen = true;
                                            break;
                                        }

                                    }

                                    if (roomOpen == false) {

                                        room.setOnClosed(new EventHandler<Event>() {
                                            @Override
                                            public void handle(Event event) {

                                                tabPane.getTabs().add(room);

                                            }
                                        });
                                    }


                                    // Sends message to selected tab, needs to change to send to tab with that ID
                                    TextArea thisArea = new TextArea();
                                    //thisArea.getUserData();
                                    AnchorPane pane = ((AnchorPane) room.getContent());

                                    //TextArea pool = ((TextArea) lol.getChildren().get(1));
                                    thisArea = (TextArea) pane.getChildren().get(1);

                                    // Appends the text the finaluser and message into the message area for the chat
                                    thisArea.appendText("[" + finalUser + "] " + msg.substring(28) + "\n");

                                    if (!msg.substring(12, 28).equals(Data.getInstance().getUser())) {
                                        sound.playSound();
                                    }

                                    break;


                                }
                            }


                        } else if (msg.substring(0, 2).equals("/u")) {

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

                        } else if (msg.substring(0, 2).equals("/a")) {

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

                            for (int i = 0; i < userList.size(); i++) {

                                if (String.valueOf(finalUser).equals(userList.get(i))) {


                                    exist = true;
                                    break;

                                } else {

                                    exist = false;


                                }
                            }

                            if (exist == false) {

                                userList.add(String.valueOf(finalUser));

                            }


                            ObservableList<String> listie = FXCollections.observableArrayList();

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    for (int q = 0; q < listie.size(); q++) {

                                        listie.remove(q);
                                    }

                                    for (int q = 0; q < userList.size(); q++) {

                                        listie.add(q, userList.get(q));
                                    }


                                    onlineUsersArea.setItems(listie);
                                }
                            });


                        } else if (msg.substring(0, 2).equals("/w")) {

                            String n = null;

                            //Saves the user name from the string into a variable
                            if (!msg.substring(2, 18).equals("null") || !msg.substring(18, 34).equals("null")) {
                                String user = msg.substring(2, 18);

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

                                //Saves the user name from the string into a variable
                                String userTwo = msg.substring(18, 34);

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

                                if (msg.substring(2, 18).equals(Data.getInstance().getUser()) || msg.substring(18, 34).equals(Data.getInstance().getUser())) {

                                    String tabId = msg.substring(34);

                                    if (user.equals(Data.getInstance().getUser())) {

                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {

                                                addTab(String.valueOf(finalUser2), tabId);
                                            }
                                        });

                                    } else {

                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {

                                                addTab(String.valueOf(finalUser1), tabId);
                                            }
                                        });

                                    }


                                }

                            }

                        } else if (msg.substring(0, 2).equals("/8")) {

                            // Create a new string builder to later save the user name in
                            StringBuilder finalUser = new StringBuilder();

                            // For loop to convert the padded username returned from the server into a username without pads
                            for (int p = 0; p < msg.substring(2).length(); p++) {

                                // Removes the * form the returned username, keeps the letters and numbers and then
                                // saves them into the StringBuilder finalUser
                                if (!String.valueOf(msg.substring(2).charAt(p)).equals("*")) {

                                    finalUser.append(String.valueOf(msg.substring(2).charAt(p)));
                                }


                            }

                            for (int i = 0; i < userList.size(); i++) {


                                if (userList.get(i).equals(String.valueOf(finalUser))) {

                                    userList.remove(i);
                                }

                            }

                            ObservableList<String> onlineUserList = FXCollections.observableArrayList();

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    for (int q = 0; q < onlineUserList.size(); q++) {

                                        onlineUserList.remove(q);
                                    }

                                    for (int q = 0; q < userList.size(); q++) {

                                        onlineUserList.add(q, userList.get(q));

                                    }


                                    onlineUsersArea.setItems(onlineUserList);
                                }
                            });

                        } else if (msg.substring(0,2).equals("/b")) {

                            System.out.println("hej");
                            if (msg.substring(2).equals(Data.getInstance().getUser())) {

                                forceLogout();
                            }
                        }


                    } catch (Exception e) {

                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    }
                }

            });

            thread.start();

        } catch (NullPointerException nPE) {

            nPE.getSuppressed();

        }
    }


    private void setGuiDesign() {

        pane.setStyle("-fx-background-color: WHITE");
        tabPane.setStyle("-fx-background-color: WHITE");

        //chatBackground.setImage(GUI.setBackgroundImage());
        chatBackground1.setImage(GUI.setBackgroundImage());

        messageField.setStyle("-fx-background-color: WHITE; -fx-background-radius: 16px;");

        sendButton.setStyle(GUI.setButtonStyle());
        logoutButton.setStyle(GUI.setButtonStyle());

        settingImageButton.setImage(GUI.setSettingImage());

    }

    public void rotateSettingForward() {

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

    public void rotateSettingBackward() {

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

    private void addGeneralTab() {

        tabPane.getTabs().add(GUI.addGeneralTab());
    }

    //Search method for Online users
    @FXML
    public void searchView(){

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                ObservableList<String> observableList = FXCollections.observableArrayList(userList);

                searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
                    onlineUsersArea.getItems().clear();

                    for (int j = 0; j < observableList.size(); j++) {

                        if (observableList.get(j).contains(searchField.getCharacters())){
                            onlineUsersArea.getItems().add(observableList.get(j));

                        }
                    }


                    if (searchField.getText().length() <= 0) {

                        dataStream.sendDataStream("/a1");
                    }
                }));

            }
        });



    }

    private void forceLogout () {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {

                    dataStream.sendDataStream("/0" + dataStream.getSocketPort() + Data.getInstance().getUser());

                    running = false;
                    userList.clear();
                    GUI.emptyTabHandler();
                    //Sets current user to null
                    //setCurrentUser(null);
                    userClass = new User(null);

                    Button button = new Button("test");
                    button.setVisible(false);
                    button.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {

                            try {

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("loginSample.fxml"));
                                Parent root = loader.load();
                                Scene scene = new Scene(root, 1200, 700);
                                Stage stage = (Stage) logoutButton.getScene().getWindow();
                                stage.setScene(scene);
                                stage.show();

                                dataStream.disconnectFromServer();

                                Alert banned = new Alert(Alert.AlertType.INFORMATION);
                                banned.setTitle("Banned");
                                banned.setHeaderText("You have been banned!");
                                banned.setContentText("You have been banned for bad behaviour");
                                banned.show();

                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                        }
                    });

                    button.fire();





                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
