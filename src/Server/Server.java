package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import static javafx.scene.input.KeyCode.T;


public class Server {

    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerProtocol serverProtocol = new ServerProtocol();

    public static void main(String[] args) {

        try {

            new Server().startThread();

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void startThread() throws IOException {


        ServerSocket ss = new ServerSocket(8080);

        while (true) {


            try {


                Socket s = ss.accept();
                System.out.println("Connected to: " + ss);
                System.out.println("Assigning new thread");

                clientHandlers.add(new ClientHandler(s));


            } catch (Exception e) {

                e.printStackTrace();

            }
        }

    }

    private void sendToClient(String message) {

        for (ClientHandler c : clientHandlers) {

            c.sendDataStream(c, message);

        }

    }

    class ClientHandler extends Thread {

        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket s;

        private volatile boolean running = true;

        ClientHandler(Socket s) throws IOException {

            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());

            this.s = s;

            start();
        }

        public void sendDataStream(ClientHandler client, String msg) {

            try {


                this.s = client.s;

                    dos.writeUTF(msg);



            } catch (SocketException sE) {

                sE.getCause();

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        @Override
        public void run()  {

            try {

                while (running) {

                    String recieved = dis.readUTF();
                    String command = recieved.substring(0,2);
                    String data = recieved.substring(2, recieved.length());

                    if (command.equals("/1") || command.equals("/2") || command.equals("/3") || command.equals("/j")
                            || command.equals("/k") || command.equals("/b") || command.equals("/a")
                            || command.equals("/d") || command.equals("/p") || command.equals("/w")
                            || command.equals("/4") || command.equals("/5")){

                        if (String.valueOf(serverProtocol.databaseProtocol(command, data)).equals("false")) {

                            dos.writeUTF("false");

                        } else if (String.valueOf(serverProtocol.databaseProtocol(command, data)).equals("true")) {

                            dos.writeUTF("true");
                        }

                        running = false;

                    } else {



                    }

                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }

}

