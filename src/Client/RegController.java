package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegController implements Initializable {

    DataStream dataStream = new DataStream();
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField usernameField, emailField, confirmEmailField;
    @FXML
    private PasswordField passwordField, confirmPasswordField;
    private ArrayList<User> userList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        pane.setStyle("-fx-background-color: WHITE");

    }

    @FXML
    public void cancel(ActionEvent event) throws IOException {

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginSample.fxml"));
        Parent root = loader.load();

        LoginController cOne = loader.getController();
        for (int i = 0; i < userList.size(); i++) {

            cOne.setData(userList.get(i));
            System.out.println(userList.get(i).getUsername());

        }

        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);

    }

    @FXML
    public void registerComplete(ActionEvent event) throws IOException {


        if (passwordField.getText().equals(confirmPasswordField.getText()) && emailField.getText().equals(
                confirmEmailField.getText())) {

            if (passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty() || emailField.getText().isEmpty()
                    || confirmEmailField.getText().isEmpty() || usernameField.getText().isEmpty()) {

                Alert accountError = new Alert(Alert.AlertType.INFORMATION);
                accountError.setTitle("Registration not complete!");
                accountError.setHeaderText("Enter information");
                accountError.setContentText("Please enter your information in all of the textfields");
                accountError.show();
            } else {

                if (usernamelength(usernameField.getText())) {

                    String username = String.format("%-16s", usernameField.getText()).replace(' ', '*');
                    String password = String.format("%-20s", passwordField.getText()).replace(' ', '*');

                    String user = "/1" + username + password + emailField.getText();

                    sendToServer(user);

                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("loginSample.fxml"));
                    Parent root = loader.load();

                    Scene scene = new Scene(root, 1200, 700);
                    stage.setScene(scene);
                } else {
                    Alert accountError = new Alert(Alert.AlertType.INFORMATION);
                    accountError.setTitle("Username to short/long");
                    accountError.setHeaderText("Username has to be between 3-16 characters");
                    accountError.setContentText("Please enter username again");
                    accountError.show();
                }
            }


        } else {

            Alert accountError = new Alert(Alert.AlertType.INFORMATION);
            accountError.setTitle("Registration not complete!");
            accountError.setHeaderText("Password/E-Mail has to match.");
            accountError.setContentText("Please enter the same password and e-mail!");
            accountError.show();
        }


    }

    public void setData(User u) {

        userList.add(u);
    }

    public boolean usernamelength(String name) {
        return name.matches("[a-zA-Z0-9]{3,16}");
    }

    public void sendToServer(String user) {

        dataStream.connectToServer();
        dataStream.sendDataStream(user);

        try {

            if (dataStream.recieveDataStream().equals("false")) {

                Alert accountError = new Alert(Alert.AlertType.INFORMATION);
                accountError.setTitle("Registration not complete!");
                accountError.setHeaderText("Username or E-Mail already exists.");
                accountError.show();

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        dataStream.disconnectFromServer();


    }

}
