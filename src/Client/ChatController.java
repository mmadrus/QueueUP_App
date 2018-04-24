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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    ArrayList<User> userList = new ArrayList<>();
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

    DataStream dataStream = new DataStream();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setStyle("-fx-background-color: WHITE");
        tabPane.setStyle("-fx-background-color: WHITE");

        dataStream.connectToServer();


        updateChat();


    }

    @FXML
    public void logout(ActionEvent event) throws IOException {

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginSample.fxml"));
        Parent root = loader.load();

        LoginController cOne = loader.getController();
        for (int i = 0; i < userList.size(); i++) {

            cOne.setData(userList.get(i));

        }

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
    }

    @FXML
    public void send(ActionEvent event) {

        if (!messageField.getText().isEmpty()) {


            dataStream.sendDataStream(messageField.getText());

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

                        messageArea.appendText(dataStream.recieveDataStream() + "\n");


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
