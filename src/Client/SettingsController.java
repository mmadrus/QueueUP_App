package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;



public class SettingsController implements Initializable {

    @FXML private Button backButton, changePasswordButton, unbanUserButton;
    @FXML private PasswordField newPasswordField;
    @FXML private TextField unbanUserField;
    @FXML private ImageView imageView;

    private GUI GUI = new GUI();
    private DataStream dataStream = new DataStream();
    private Admin admin = new Admin();
    private String user = Data.getInstance().getUser();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGuiDesign();

        admin.addAdmin();

        boolean isAdmin = false;

        System.out.println("settings: " + user);
        for (int i = 0; i < admin.getAdminList().size(); i++){

            if (Data.getInstance().getUser().equals(admin.getAdminList().get(i))) {

                isAdmin = true;
                break;

            } else {

                isAdmin = false;

            }
        }

        if (isAdmin == true) {

            unbanUserButton.setVisible(true);
            unbanUserField.setVisible(true);

        } else {

            unbanUserField.setVisible(false);
            unbanUserButton.setVisible(false);

        }
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

    @FXML
    public void unbanUser (ActionEvent event) {

        String username = String.format("%-16s", unbanUserField.getText()).replace(' ', '*');

        try {

            dataStream.connectToServer();

            dataStream.sendDataStream("/4" + username);

            if (dataStream.recieveDataStream().equals("false")) {

                Alert passwordChange = new Alert(Alert.AlertType.INFORMATION);
                passwordChange.setTitle("Failure");
                passwordChange.setHeaderText("Unban session did not go throught.");
                passwordChange.setContentText("Check server for error.");
                passwordChange.show();

                dataStream.disconnectFromServer();

            } else {

                Alert passwordChange = new Alert(Alert.AlertType.INFORMATION);
                passwordChange.setTitle("Success!");
                passwordChange.setHeaderText(unbanUserField.getText() + " is no longer banned.");
                //passwordChange.setContentText("Server is down");
                passwordChange.show();
                dataStream.disconnectFromServer();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    private void setGuiDesign () {

        imageView.setImage(GUI.setBackgroundImage());
        newPasswordField.setStyle(GUI.setTextfieldStyle());
        unbanUserField.setStyle(GUI.setTextfieldStyle());
        backButton.setStyle(GUI.setButtonStyle());
        changePasswordButton.setStyle(GUI.setButtonStyle());
        unbanUserButton.setStyle(GUI.setButtonStyle());

    }


}
