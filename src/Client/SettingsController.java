package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;



public class SettingsController implements Initializable {

    @FXML private Button backButton, changePasswordButton;
    @FXML private PasswordField newPasswordField;

    private GUI GUI = new GUI();
    private DataStream dataStream = new DataStream();
    private User user;
    private String currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGuiDesign();
    }

    @FXML
    public void backToChat (javafx.event.ActionEvent event) {

        try {

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @FXML
    public void changePassword (ActionEvent event) {

        try {

            if (!newPasswordField.getText().isEmpty() && newPasswordField.getText().length() < 20) {

                String password = String.format("%-20s", newPasswordField.getText()).replace(' ', '*');

                dataStream.connectToServer();
                dataStream.sendDataStream("/c" + Data.getInstance().getUser() + password);

                if (dataStream.recieveDataStream().equals("false")) {

                    Alert passwordChange = new Alert(Alert.AlertType.INFORMATION);
                    passwordChange.setTitle("Failure");
                    passwordChange.setHeaderText("Your password did not get updated");
                    //passwordChange.setContentText("Server is down");
                    passwordChange.show();

                    dataStream.disconnectFromServer();

                } else {

                    Alert passwordChange = new Alert(Alert.AlertType.INFORMATION);
                    passwordChange.setTitle("Success!");
                    passwordChange.setHeaderText("Your password is now updated!");
                    //passwordChange.setContentText("Server is down");
                    passwordChange.show();
                    dataStream.disconnectFromServer();
                }
            } else {

                Alert passwordChange = new Alert(Alert.AlertType.INFORMATION);
                passwordChange.setTitle("Failure");
                passwordChange.setHeaderText("The field can not be empty and maximum of 20 characters.");
                //passwordChange.setContentText("Server is down");
                passwordChange.show();

            }

            } catch(Exception e){

                e.printStackTrace();

            }




    }

    private void setGuiDesign () {

        backButton.setStyle(GUI.setButtonStyle());
        changePasswordButton.setStyle(GUI.setButtonStyle());

    }


}
