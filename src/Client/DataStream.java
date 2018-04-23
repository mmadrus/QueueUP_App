package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataStream extends Thread {

    private Socket s;
    private DataOutputStream dos;
    private DataInputStream dis;

    public void sendMessage(String user, String msg) {

        try {
            dos.writeUTF("[" + user + "] " + msg);



        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public String recieveMessage () throws IOException {

        return dis.readUTF();
    }


    public void connectToServer() {

        try {

            s = new Socket("ua-83-226-35-166.cust.bredbandsbolaget.se", 8080);

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

