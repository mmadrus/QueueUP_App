package Client;

import javafx.scene.control.CheckBox;

import java.io.IOException;
import java.net.Socket;

public class Data {

    private User user;
    private DataStream dataStream = new DataStream();
    private Sound sound = new Sound();
    
    private static Data ourInstance = new Data();

    public static Data getInstance() {
        return ourInstance;
    }

    private Data() {
    }

    protected void setUser (String username) {

        user = new User(username);
    }

    protected String getUser () {

        return user.getCurrentUser();
    }

    protected void connect () {

        dataStream.connectToServer();
    }

    protected void disconnect () {

        dataStream.disconnectFromServer();
    }

    protected void send (String data) {

        dataStream.sendDataStream(data);
    }

    protected String recieve () throws IOException {

        return dataStream.recieveDataStream();
    }

    protected String getSocket () {

        return String.valueOf(dataStream.getSocketPort());
    }

    protected void setSound (boolean soundBoolean) {

        sound.setSoundOn(soundBoolean);
    }

    protected boolean getSound () {

        return sound.isSoundOn();
    }




}
