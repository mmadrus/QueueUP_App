package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
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

public class Controller implements Initializable {

    String hej = "svejs";

    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    ArrayList<User> userList = new ArrayList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setStyle("-fx-background-color: WHITE");
        File file = new File("resources/icon.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);


    }

    @FXML
    public void registerAccount(ActionEvent event) throws IOException {

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sampleTwo.fxml"));
        Parent root = loader.load();

        ControllerTwo cTwo = loader.getController();
        for (int i = 0; i < userList.size(); i++) {

            cTwo.setData(userList.get(i));

        }

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
    }

    @FXML
    public void login(ActionEvent event) throws IOException {

        for (int i = 0; i < userList.size(); i++) {


            if (usernameField.getText().equals(userList.get(i).getUsername()) &&
                    passwordField.getText().equals(userList.get(i).getPassword())) {

                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("sampleThree.fxml"));
                Parent root = loader.load();

                ControllerThree controllerThree = loader.getController();
                for (int x = 0; x < userList.size(); x++) {

                    controllerThree.setData(userList.get(x));

                }

                controllerThree.setCurrentUser(usernameField.getText());
                controllerThree.setChannelUser(usernameField.getText());


                Scene scene = new Scene(root, 1200, 700);
                stage.setScene(scene);

            } else {

                System.out.println("error");
            }
        }

    }


    public void setData (User u){

        userList.add(u);
    }

}
