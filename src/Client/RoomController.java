package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.DoubleAccumulator;

public class RoomController implements Initializable {

    @FXML
    private Button backButton, createRoomButton;
    @FXML
    private TextField roomNameField;
    @FXML
    private PasswordField roomPasswordField;
    @FXML
    private ImageView imageView;

    private GUI gui = new GUI();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setGui();
    }

    @FXML
    public void backToChat (ActionEvent event) {

        try {

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @FXML
    public void createRoom () {


        try {

            if (!roomNameField.getText().isEmpty() && roomNameField.getText().length() < 20) {

                String roomName = String.format("%-20s", roomNameField.getText()).replace(' ', '*');
                String user = Data.getInstance().getUser();

                if (roomPasswordField.getText().isEmpty()) {



                    Data.getInstance().send("/r" + user + roomName);

                    Stage stage = (Stage) backButton.getScene().getWindow();

                    stage.close();


                } else if (!roomPasswordField.getText().isEmpty()) {

                    String password = String.format("%-20s", roomPasswordField.getText()).replace(' ', '*');

                    Data.getInstance().send("/p" + roomName + password);

                    Stage stage = (Stage) backButton.getScene().getWindow();

                    stage.close();


                } else {

                    //error handling
                }

            } else {

                //error handling
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void setGui () {

        backButton.setStyle(gui.setButtonStyle());
        createRoomButton.setStyle(gui.setButtonStyle());
        roomNameField.setStyle(gui.setTextfieldStyle());
        roomPasswordField.setStyle(gui.setTextfieldStyle());
        imageView.setImage(gui.setBackgroundImage());
    }

}
