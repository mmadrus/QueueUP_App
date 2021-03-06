package Client;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class GUI {

    private ArrayList<Tab> currentTabs = new ArrayList<>();
    private ArrayList<Tab> hiddenTabs = new ArrayList<>();
    private ArrayList<Tab> hiddenPrivateRoomTabs = new ArrayList<>();


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

    public Image setSettingImage () {

        File file = new File("resources/settingsImage.png");
        Image image = new Image(file.toURI().toString());

        return image;


    }

    public Tab createNewTab (String name, String id) {


        Tab newTab = new Tab();
        newTab.setText("#" + name);
        newTab.setClosable(false);
        newTab.setDisable(false);
        newTab.setUserData(id);

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
        newTextArea.setUserData(id);

        AnchorPane.setTopAnchor(imageView,0.0);
        AnchorPane.setTopAnchor(newTextArea, 0.0);

        newPane.getChildren().addAll(imageView, newTextArea);

        currentTabs.add(newTab);

        newTab.setContent(newPane);


        return newTab;

    }

    public ArrayList<Tab> getTabHandler() {
        return currentTabs;
    }

    public Tab getTab (int i) {

        return currentTabs.get(i);
    }

    public Tab addGeneralTab () {

        Tab newTab = new Tab();
        newTab.setText("#General");
        newTab.setClosable(false);
        newTab.setUserData("1000000001");

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
        newTextArea.setUserData("1000000001");

        AnchorPane.setTopAnchor(imageView,0.0);
        AnchorPane.setTopAnchor(newTextArea, 0.0);

        newPane.getChildren().addAll(imageView, newTextArea);

        currentTabs.add(newTab);
        hiddenTabs.add(newTab);

        newTab.setContent(newPane);


        return newTab;


    }

    public Tab addHiddenTab (String name, String id) {


        Tab newTab = new Tab();
        newTab.setText("#" + name);
        newTab.setClosable(true);
        newTab.setDisable(false);
        newTab.setUserData(id);
        newTab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {

                currentTabs.remove(newTab);
            }
        });

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
        newTextArea.setUserData(id);

        AnchorPane.setTopAnchor(imageView,0.0);
        AnchorPane.setTopAnchor(newTextArea, 0.0);

        newPane.getChildren().addAll(imageView, newTextArea);

        hiddenTabs.add(newTab);

        newTab.setContent(newPane);


        return newTab;

    }

    public Tab addHiddenPrivateTab (String name, String id, String pword) {

        Tab newTab = new Tab();
        newTab.setText("#" + name);
        newTab.setClosable(true);
        newTab.setDisable(false);
        newTab.setUserData(id);
        newTab.setId(pword);
        newTab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {

                currentTabs.remove(newTab);
            }
        });

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
        newTextArea.setUserData(id);

        AnchorPane.setTopAnchor(imageView,0.0);
        AnchorPane.setTopAnchor(newTextArea, 0.0);

        newPane.getChildren().addAll(imageView, newTextArea);

        hiddenPrivateRoomTabs.add(newTab);

        newTab.setContent(newPane);


        return newTab;

    }

    public String getHidden (int i) {

        return hiddenTabs.get(i).getText();
    }

    public int getHiddenTabSize () {

        return hiddenTabs.size();
    }

    public int getHiddenPrivateSize () {

        return hiddenPrivateRoomTabs.size();
    }

    public String getHiddenPrivate (int i) {

        return hiddenPrivateRoomTabs.get(i).getText();
    }

    public void emptyTabHandler () {

        currentTabs.clear();
    }

    public void makeTabVisable (String room) {

        for (Tab t: hiddenTabs) {

            if (t.getText().equals(room)) {

                currentTabs.add(t);
                break;

            }
        }

        for (Tab t: hiddenPrivateRoomTabs) {

            if (t.getText().equals(room)) {

                currentTabs.add(t);
                break;
            }
        }

    }

    public Tab addHelpTab (String name, String id) {

        Tab newTab = new Tab();
        newTab.setText(name);
        newTab.setClosable(false);
        newTab.setDisable(false);
        newTab.setUserData(id);

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
        newTextArea.setUserData(id);

        AnchorPane.setTopAnchor(imageView,0.0);
        AnchorPane.setTopAnchor(newTextArea, 0.0);

        newPane.getChildren().addAll(imageView, newTextArea);

        currentTabs.add(newTab);

        newTab.setContent(newPane);


        return newTab;


    }


}
