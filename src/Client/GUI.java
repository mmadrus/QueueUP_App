package Client;

import javafx.scene.image.Image;

import java.io.File;

public class GUI {

    public String setButtonStyle() {

        String buttonDesign = "-fx-base: #4a86e8; -fx-text-fill: WHITE; " +
                "-fx-background-radius: 16px;";


        return buttonDesign;
    }

    public String setTextfieldStyle() {

        String style = "-fx-background-color: WHITE, -fx-background;" +
                "-fx-background-insets: 0, 0 0 1 0 ;" +
                "-fx-background-radius: 40;";

        return style;
    }

    public Image setBackgroundImage () {

        File file = new File("resources/icon.png");
        Image image = new Image(file.toURI().toString());

        return image;
    }
}
