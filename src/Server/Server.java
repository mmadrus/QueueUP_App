package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server  {

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(8000);

        while (true) {

            Socket s = null;

            try {

                s = ss.accept();
                System.out.println("Connected to: " + ss);
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread");

                Thread t = new ClientHandler(s, dis, dos);

                t.start();


            } catch (Exception e) {

                s.close();
                e.printStackTrace();

            }
        }


    }
}

class ClientHandler extends Thread {


    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run() {

        String recieved;

        while (true) {

            try {

                recieved = dis.readUTF();
                dos.writeUTF(recieved);

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }
}