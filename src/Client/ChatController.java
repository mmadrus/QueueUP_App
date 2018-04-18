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
import java.net.InetAddress;
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
    private TextArea messageArea, channelUser;
    private String currentUser;
    private Socket s;
    private DataOutputStream dos;
    private DataInputStream dis;




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setStyle("-fx-background-color: WHITE");
        tabPane.setStyle("-fx-background-color: WHITE");

        updateMessages();

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
            s.close();

        }

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
    }

    @FXML
    public void sendMessage(ActionEvent event) {

        new MessageThread().send();


    }

    public void setData(User u) {

        userList.add(u);
    }

    public void setChannelUser(String name) {

        try {

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            dos.writeUTF(name);

            String recievedName = dis.readUTF();

            channelUser.appendText(recievedName);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String setCurrentUser(String user) {

        currentUser = user;

        return user;
    }


    public void connectToServer() {

        try {

            s = new Socket("ua-83-226-35-166.cust.bredbandsbolaget.se", 8080);

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void updateMessages() {

        Thread thread = new Thread(() -> {

            while (true) {

                new MessageThread().recieve();

                if (false) {

                    new Thread().stop();
                }
            }

        });

        thread.start();

    }


    class MessageThread extends Thread {

        public void send() {

            try {

                if (!messageField.getText().isEmpty()) {

                    dos.writeUTF("[" + currentUser + "] " + messageField.getText());

                    messageField.clear();

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

        public void recieve() {

            try {

                String recieved = dis.readUTF();

                messageArea.appendText(recieved + "\n");


            } catch (Exception e) {

                e.getSuppressed();

            }

        }


    }
}
