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

    ArrayList<User> userList = new ArrayList<>();
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

        dataStream.connectToServer();


        updateChat();


    }

    @FXML
    public void logout(ActionEvent event) throws IOException {

        dataStream.disconnectFromServer();
        setCurrentUser(null);

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginSample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
    }

    @FXML
    public void send(ActionEvent event) {

        if (!messageField.getText().isEmpty()) {


            dataStream.sendDataStream("/m" + "[" + currentUser + "] " + messageField.getText());

            messageField.clear();

        }


    }

    public void setData(User u) {

        userList.add(u);
    }

    public String setCurrentUser(String user) {

        currentUser = user;

        return user;
    }

    public void updateChat() {

        try {
            Thread thread = new Thread(() -> {

                while (true) {

                    try {

                        String msg = dataStream.recieveDataStream();

                        if (msg.substring(0,2).equals("/m")) {

                            String user = msg.substring(3,19);
                            StringBuilder finalUser = new StringBuilder();

                            for (int p = 0; p < user.length(); p++) {

                                if (!String.valueOf(user.charAt(p)).equals("*")) {

                                    finalUser.append(String.valueOf(user.charAt(p)));
                                }

                            }

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
