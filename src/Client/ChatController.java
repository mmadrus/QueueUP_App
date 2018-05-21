package Client;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private TabPane tabPane;
    @FXML
    private TextField messageField, searchField;
    @FXML
    private Button sendButton, logoutButton;
    @FXML
    private ImageView chatBackground1, settingImageButton;
    @FXML
    private ListView<String> onlineUsersArea, channelList;
    @FXML
    private Tab currentTab, helpMessageTab;

    private volatile boolean running = true;
    private GUI GUI = new GUI();
    private ArrayList<String> userList = new ArrayList<>();
    private Sound sound = new Sound();
    private Admin admin = new Admin();
    private ObservableList<String> tabs = FXCollections.observableArrayList();
    private Button tabBtn = new Button();
    private Tab newTab = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setGuiDesign();
        addGeneralTab();
        tabs.add(GUI.getTab(0).getText());
        GUI.addHelpTab(helpMessageTab.getText(), "1000000002");
        Button btn = updateChannelListButton();
        // Connects to the server when the scene is initialized
        Data.getInstance().connect();

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> currentTab = newValue);
        admin.addAdmin();
        // Creates a thread that constantly updates the chat
        updateChat();

        btn.fire();


    }

    // If the user wants to log out
    @FXML
    public void handleLogoutButton(ActionEvent event) throws IOException {

        Data.getInstance().send("/0" + Data.getInstance().getSocket() + Data.getInstance().getUser());

        running = false;
        userList.clear();
        GUI.emptyTabHandler();
        //Sets current user to null
        //setCurrentUser(null);
        Data.getInstance().setUser(null);

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginSample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);

        Data.getInstance().disconnect();
    }

    @FXML
    public void handleSettingsButton(MouseEvent event) {

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

            System.out.println("userdata: " + thisArea.getUserData());
            // Creates a string with the message command, current user and the message, then sends it to the server
            Data.getInstance().send("/m" + thisArea.getUserData() + Data.getInstance().getUser() + messageField.getText());

            messageField.clear();

        }


    }

    @FXML
    public void addTab(String room) {

        boolean exists = true;

        for (int c = 0; c < GUI.getTabHandler().size(); c++) {

            System.out.println("Tab: " + GUI.getTab(c).getText());
            for (int i = 0; i < tabPane.getTabs().size(); i++) {

                if (tabPane.getTabs().get(i).getText().equals(room)) {

                    exists = true;
                    break;

                } else {

                    exists = false;
                }

            }


            if (exists == false) {

                //tabPane.getTabs().add(GUI.getTab(c));

                if (!GUI.getTab(c).getText().equals("#Help")) {

                    newTab = GUI.getTab(c);

                    if (newTab.getUserData().toString().substring(0, 2).equals("12")) {

                        addNewPrivateRoomTab(newTab);


                    } else {

                        addNewPublicRoomTab(newTab);


                    }


                }
            }


        }
    }


    @FXML
    public void popupMenuUser(MouseEvent event) {
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
                            Data.getInstance().send("/w" + Data.getInstance().getUser() + wantedUsername);
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
                            Data.getInstance().send("/w" + Data.getInstance().getUser() + wantedUsername);
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
                    Data.getInstance().send("/b" + paddedUser);
                }
            });

        }

    }

    @FXML
    public void popupMenuChannel(MouseEvent event) {
        MenuItem joinChannel = new MenuItem("Join channel");
        MenuItem createChannel = new MenuItem("Create channel");
        MenuItem closeMenu = new MenuItem("Close");

        ContextMenu contextMenuUser1 = new ContextMenu();

        contextMenuUser1.getItems().addAll(joinChannel, createChannel, closeMenu);
        contextMenuUser1.setAutoHide(true);
        contextMenuUser1.show(pane, event.getScreenX(), event.getScreenY());

        closeMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                contextMenuUser1.hide();
            }
        });

        joinChannel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String roomName = channelList.getSelectionModel().getSelectedItem();
                String finalRoom = roomName;
                System.out.println("FR: " + finalRoom);

                GUI.makeTabVisable(finalRoom);
                addTab(finalRoom);
                tabBtn.fire();


            }
        });

        createChannel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {

                    Stage stage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("newRoomSample.fxml"));

                    //Data data = Data.getInstance();
                    //data.setUser(userClass.getCurrentUser());

                    stage.setTitle("Create room");
                    stage.setScene(new Scene(root));
                    stage.show();

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });


    }

    // Method to update chat
    private void updateChat() {

        try {
            Thread thread = new Thread(() -> {

                while (running) {

                    try {

                        channelList.setItems(tabs);
                        Data.getInstance().send("/a");


                        // Saves the data stream from the server into a string
                        String msg = Data.getInstance().recieve();

                        // Checks for the command at the first two indexes

                        // If the command is /m then it is a message
                        if (msg.substring(0, 2).equals("/m")) {

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

                                    /*if (roomOpen == false) {

                                        room.setOnClosed(new EventHandler<Event>() {
                                            @Override
                                            public void handle(Event event) {

                                                tabPane.getTabs().add(room);

                                            }
                                        });
                                    }*/


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

                                                GUI.createNewTab(String.valueOf(finalUser2), tabId);
                                            }
                                        });

                                    } else {

                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {

                                                GUI.createNewTab(String.valueOf(finalUser1), tabId);
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

                        } else if (msg.substring(0, 2).equals("/b")) {

                            if (msg.substring(2).equals(Data.getInstance().getUser())) {

                                forceLogout();
                            }
                        } else if (msg.substring(0, 2).equals("/t")) {

                            System.out.println(msg);
                            if (msg.substring(2, 3).equals("1")) {

                                if (msg.substring(3, 19).equals(Data.getInstance().getUser())) {

                                    // Create a new string builder to later save the user name in
                                    StringBuilder finalRoom = new StringBuilder();

                                    // For loop to convert the padded username returned from the server into a username without pads
                                    for (int p = 0; p < msg.substring(19, 39).length(); p++) {

                                        // Removes the * form the returned username, keeps the letters and numbers and then
                                        // saves them into the StringBuilder finalUser
                                        if (!String.valueOf(msg.substring(19, 39).charAt(p)).equals("*")) {

                                            finalRoom.append(String.valueOf(msg.substring(19, 39).charAt(p)));
                                        }


                                    }

                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {

                                            tabs.remove(0, tabs.size());
                                            channelList.setItems(tabs);

                                        }
                                    });


                                    boolean exist = false;

                                    for (int i = 0; i < GUI.getHiddenTabSize(); i++) {

                                        if (String.valueOf(finalRoom).equals(GUI.getHidden(i))) {


                                            exist = true;
                                            break;

                                        } else {

                                            exist = false;


                                        }
                                    }

                                    if (exist == false) {

                                        if (!msg.substring(39, 41).equals("12")) {

                                            GUI.addHiddenTab(String.valueOf(finalRoom), msg.substring(39));

                                        } else if (msg.substring(59,61).equals("12")){

                                            // Create a new string builder to later save the user name in
                                            StringBuilder finalPassword = new StringBuilder();

                                            // For loop to convert the padded username returned from the server into a username without pads
                                            for (int p = 0; p < msg.substring(39, 59).length(); p++) {

                                                // Removes the * form the returned username, keeps the letters and numbers and then
                                                // saves them into the StringBuilder finalUser
                                                if (!String.valueOf(msg.substring(39, 59).charAt(p)).equals("*")) {

                                                    finalPassword.append(String.valueOf(msg.substring(39, 59).charAt(p)));
                                                }


                                            }


                                            System.out.println("test" + msg.substring(59));
                                            GUI.addHiddenPrivateTab(String.valueOf(finalRoom), msg.substring(59), String.valueOf(finalPassword));
                                        }

                                    }

                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                    /*for (int q = 0; q < GUI.getCurrentTabSize(); q++) {

                                        tabs.add(q, GUI.getTab(q));

                                    }*/

                                            for (int q = 0; q < GUI.getHiddenTabSize(); q++) {

                                                try {

                                                    tabs.add(q, GUI.getHidden(q));

                                                } catch (IndexOutOfBoundsException e) {

                                                    e.printStackTrace();
                                                }

                                            }

                                            for (int q = 0; q < GUI.getHiddenPrivateSize(); q++) {

                                                try {
                                                    tabs.add(tabs.size(), GUI.getHiddenPrivate(q));
                                                } catch (IndexOutOfBoundsException e) {

                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                                }

                            } else {

                                System.out.println("WTF: " + msg.substring(2, 22));
                                // Create a new string builder to later save the user name in
                                StringBuilder finalRoom = new StringBuilder();

                                // For loop to convert the padded username returned from the server into a username without pads
                                for (int p = 0; p < msg.substring(2, 22).length(); p++) {

                                    // Removes the * form the returned username, keeps the letters and numbers and then
                                    // saves them into the StringBuilder finalUser
                                    if (!String.valueOf(msg.substring(2, 22).charAt(p)).equals("*")) {

                                        finalRoom.append(String.valueOf(msg.substring(2, 22).charAt(p)));
                                        System.out.println("char at " + p + ": " + msg.substring(2, 22).charAt(p));
                                    }


                                }

                                System.out.println("Finalroom:" + String.valueOf(finalRoom));

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {

                                        tabs.clear();
                                        channelList.setItems(tabs);

                                    }
                                });


                                boolean exist = false;

                                for (int i = 0; i < GUI.getHiddenTabSize(); i++) {

                                    if (String.valueOf(finalRoom).equals(GUI.getHidden(i))) {


                                        exist = true;
                                        break;

                                    } else {

                                        exist = false;


                                    }
                                }

                                if (exist == false) {

                                    System.out.println("msg2,22: " + msg.substring(22));
                                    if (!msg.substring(22, 24).equals("12")) {

                                        GUI.addHiddenTab(String.valueOf(finalRoom), msg.substring(22));

                                    } else /*if (msg.substring(42, 44).equals("12"))*/ {

                                        boolean exists = false;

                                        System.out.println("hihi: " + msg.substring(22));

                                        if (GUI.getHiddenPrivateSize() > 0) {
                                            for (int i = 0; i < GUI.getHiddenTabSize(); i++) {

                                                if (String.valueOf(finalRoom).equals(GUI.getHiddenPrivate(i))) {


                                                    exists = true;
                                                    break;

                                                } else {

                                                    exists = false;


                                                }
                                            }
                                        }

                                        if (exists == false) {
                                            System.out.println("msg: " + msg);
                                            // Create a new string builder to later save the user name in
                                            StringBuilder finalPassword = new StringBuilder();

                                            // For loop to convert the padded username returned from the server into a username without pads
                                            for (int p = 0; p < msg.substring(22, 42).length(); p++) {

                                                // Removes the * form the returned username, keeps the letters and numbers and then
                                                // saves them into the StringBuilder finalUser
                                                if (!String.valueOf(msg.substring(22, 42).charAt(p)).equals("*")) {

                                                    finalPassword.append(String.valueOf(msg.substring(22, 42).charAt(p)));
                                                }


                                            }

                                            System.out.println("test" + msg.substring(42));
                                            System.out.println(String.valueOf(finalRoom));
                                            GUI.addHiddenPrivateTab(String.valueOf(finalRoom), msg.substring(42), String.valueOf(finalPassword));
                                        }
                                    }

                                }

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                    /*for (int q = 0; q < GUI.getCurrentTabSize(); q++) {

                                        tabs.add(q, GUI.getTab(q));

                                    }*/

                                        for (int q = 0; q < GUI.getHiddenTabSize(); q++) {

                                            tabs.add(q, GUI.getHidden(q));

                                        }

                                        for (int q = 0; q < GUI.getHiddenPrivateSize(); q++) {

                                            tabs.add(tabs.size(), GUI.getHiddenPrivate(q));
                                        }
                                    }
                                });
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

    private void forceLogout() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {

                    Data.getInstance().send("/0" + Data.getInstance().getSocket() + Data.getInstance().getUser());

                    running = false;
                    userList.clear();
                    GUI.emptyTabHandler();
                    //Sets current user to null
                    //setCurrentUser(null);
                    Data.getInstance().setUser(null);

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

                                Data.getInstance().disconnect();

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

    private Button updateChannelListButton() {

        Button btn = new Button("test");
        btn.setVisible(false);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Data.getInstance().send("/r" + "1" + Data.getInstance().getUser());
            }
        });

        return btn;
    }

    private void addNewPrivateRoomTab (Tab t) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Password");
        dialog.setHeaderText("Enter the password for the room:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && result.get().equals(t.getId())) {


            System.out.println("res: " + result.get());
            Platform.runLater(new Runnable() {
                public void run() {
                    tabPane.getTabs().add(tabPane.getTabs().size()-tabPane.getTabs().size()+1, t);
                }
            });

        }
    }

    private void addNewPublicRoomTab (Tab t) {

        String id = String.valueOf(t.getUserData());

        System.out.println(id);

        System.out.println("gets here: " + t.getText());
        Platform.runLater(new Runnable() {
            public void run() {
                try {

                    tabPane.getTabs().add(tabPane.getTabs().size()-tabPane.getTabs().size() +1, t);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });

    }

    //Search method for Online users
    @FXML
    public void searchView() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                ObservableList<String> observableList = FXCollections.observableArrayList(userList);

                searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
                    onlineUsersArea.getItems().clear();

                    for (int j = 0; j < observableList.size(); j++) {

                        if (observableList.get(j).contains(searchField.getCharacters())) {
                            onlineUsersArea.getItems().add(observableList.get(j));

                        }
                    }


                    if (searchField.getText().length() <= 0) {

                        Data.getInstance().send("/a1");
                    }
                }));

            }
        });


    }


}
