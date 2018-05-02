package Client;

import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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

    public Tab createNewTab () {

        Tab newTab = new Tab();
        newTab.setText("New Tab");
        newTab.isClosable();

        AnchorPane newPane = new AnchorPane();
        newPane.setMinWidth(0.0);
        newPane.setMinHeight(0.0);
        newPane.setPrefHeight(180);
        newPane.setPrefWidth(200);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(563);
        imageView.setFitWidth(800);
        imageView.setPreserveRatio(true);
        imageView.setImage(setBackgroundImage());

        TextArea newTextArea = new TextArea();
        newTextArea.setOpacity(0.75);
        newTextArea.setEditable(false);
        newTextArea.setWrapText(true);
        newTextArea.setPrefWidth(800);
        newTextArea.setPrefHeight(563);

        AnchorPane.setTopAnchor(imageView,0.0);
        AnchorPane.setTopAnchor(newTextArea, 0.0);

        newPane.getChildren().addAll(imageView, newTextArea);

        newTab.setContent(newPane);

        return newTab;

    }
}
