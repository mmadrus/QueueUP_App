package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataStream extends Thread {

    // Variables for the socket and input/output data streams

    private Socket s;
    private DataOutputStream dos;
    private DataInputStream dis;

    // Sends string to the server
    public void sendDataStream(String data) {

        try {

            dos.writeUTF(data);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // Receives strings from the server
    public String recieveDataStream() throws IOException {

        return dis.readUTF();
    }


    // Creates a connection to the server
    public void connectToServer() {

        try {

            // Conncects to the server host and the port for the server
            s = new Socket("hkrwlan-32-28.clients.hkr.se" +
                    "", 8080);

            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // Method to close connection to server
    public void disconnectFromServer() {

        try {

            s.close();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public int getSocketPort () {

        return s.getLocalPort();
    }
}

