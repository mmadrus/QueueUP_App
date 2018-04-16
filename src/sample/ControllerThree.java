package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

public class ControllerThree implements Initializable {

    String s;

    @FXML private AnchorPane pane;

    @FXML private TabPane tabPane;

    @FXML private TextField messageField;

    @FXML private TextArea messageArea, channelUser;

    private String currentUser;

    ArrayList<User> userList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setStyle("-fx-background-color: WHITE");
        tabPane.setStyle("-fx-background-color: WHITE");

    }

    @FXML
    public void logout (ActionEvent event) throws IOException {

        Node node = (Node)event.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        Controller cOne = loader.getController();
        for (int i = 0; i < userList.size(); i++) {

            cOne.setData(userList.get(i));
            System.out.println(userList.get(i).getUsername());

        }

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
    }

    @FXML
    public void sendMessage (ActionEvent event) {

        try {
            if (!messageField.getText().isEmpty()){
                messageArea.appendText("[" + currentUser + "] " + messageField.getText() + "\n");

                messageField.clear();
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void setData (User u){

        userList.add(u);
    }

    public void setChannelUser(String name) {

        channelUser.appendText(name);
    }

    public String setCurrentUser(String user) {

        currentUser = user;

        return  user;
    }
}
