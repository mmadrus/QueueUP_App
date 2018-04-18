package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {


    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) {

        try {

            new Server().startThread();

        } catch (Exception e) {

            e.printStackTrace();
        }



    }

    public void startThread() throws IOException {

        ServerSocket ss = new ServerSocket(8000);

        while (true) {


            try {

                Socket s = ss.accept();
                System.out.println("Connected to: " + ss);
                System.out.println("Assigning new thread");

                ClientHandler c = new ClientHandler(s);
                clientHandlers.add(c);


            } catch (Exception e) {

                e.printStackTrace();

            }
        }

    }

    public void sendAll(String message) {

        for (ClientHandler c : clientHandlers) {

            c.sendMessages(c, message);

        }

    }

    class ClientHandler extends Thread {

        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket s;

        ClientHandler(Socket s) throws IOException {

            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());

            this.s = s;

            start();
        }

        public void sendMessages(ClientHandler client, String msg) {

            try {


                this.s = client.s;
                dos.writeUTF(msg);

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            String recieved;

            try {

                while (true) {

                    recieved = dis.readUTF();

                    sendAll(recieved);

                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }

}

