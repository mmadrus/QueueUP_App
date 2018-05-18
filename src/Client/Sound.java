package Client;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sound {

    private String soundFile = "resources/coffesound.mp3";

    private double volume = 0.2;

    public void playSound () {

        Media sound = new Media(new File(soundFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
