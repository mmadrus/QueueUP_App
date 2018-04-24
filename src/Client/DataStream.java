package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataStream extends Thread {

    private Socket s;
    private DataOutputStream dos;
    private DataInputStream dis;

    public void sendDataStream(String data) {

        try {

            dos.writeUTF(data);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public String recieveDataStream () throws IOException {

        return dis.readUTF();
    }


    public void connectToServer() {

        try {

            s = new Socket("hkrwlan-41-39.clients.hkr.se", 8080);

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void disconnectFromServer () {

        try {

            s.close();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
}

