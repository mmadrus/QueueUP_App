package Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class SettingsController implements Initializable {

    @FXML private Button backButton, changePasswordButton;

    private GUI GUI = new GUI();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGuiDesign();
    }

    public void backToChat (javafx.event.ActionEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatSample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
    }

    private void setGuiDesign () {

        backButton.setStyle(GUI.setButtonStyle());
        changePasswordButton.setStyle(GUI.setButtonStyle());

    }

}
