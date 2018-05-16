package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class Server {

    // Variables to save all threads connected and creates a server protocol object
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private ServerProtocol serverProtocol = new ServerProtocol();

    private int counter = 0;

    // Runs when server is started
    public static void main(String[] args) {

        try {

            // Method to start threads
            new Server().startThread();

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    // Method to start the server and let threads connect to it
    public void startThread() throws IOException {


        ServerSocket ss = new ServerSocket(8080);

        while (true) {


            try {


                Socket s = ss.accept();
                String str = ss.toString();
                System.out.println("Connected to: " + s);
                System.out.println("Assigning new thread");

                System.out.println(str);
                System.out.println(s.toString());

                // Saves the connected thread into an arraylist
                clientHandlers.add(new ClientHandler(s));


            } catch (Exception e) {

                e.printStackTrace();

            }
        }

    }

    // Method to send the data stream to all clients with the sendDataStream method in the ClientHandler class
    private void sendToClient(String message) {

        for (ClientHandler c : clientHandlers) {

            c.sendDataStream(c, message);

        }

    }

    private void sendOnlineListToClient () {

        for (ClientHandler c: clientHandlers) {

            for (String u: serverProtocol.onlineUsers){

                c.sendDataStream(c, "/a" + u);
                System.out.println();
            }
        }
    }

    // Nested class in the server class
    class ClientHandler extends Thread {

        private DataInputStream dis;
        private DataOutputStream dos;
        private Socket s;
        //private String currentUser;

        private volatile boolean running = true;

        // Constructor for the ClientHandler class which only saves the socket the connected thread belongs to
        ClientHandler(Socket s) throws IOException {

            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());

            this.s = s;
            //this.currentUser = currentUser;

            start();
        }

        // Send the data stream to the client, takes the client socket and the message as parameters
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

        // This methods receives data from the clients and then checks for commands
        @Override
        public void run() {

            try {

                // Infinite loop to make it always receive information from the clients
                while (running) {

                    //Saves the data stream from the client into a string
                    String recieved = dis.readUTF();

                    // Saves the first to characters (the command) into a string
                    String command = recieved.substring(0, 2);

                    // Saves the rest into another string
                    String data = recieved.substring(2, recieved.length());

                    // Checks for the commands that have something to do with the database
                    if (command.equals("/1") || command.equals("/2") || command.equals("/3") || command.equals("/j")
                            || command.equals("/k") || command.equals("/b")
                            || command.equals("/d") || command.equals("/p")
                            || command.equals("/4") || command.equals("/5") || command.equals("/6")) {

                        // Checks for the return statement from the protocol, if false then it send it to the client
                        if (String.valueOf(serverProtocol.databaseProtocol(command, data)).equals("false")) {

                            dos.writeUTF("false");

                        } else { // Else it sends true to the client

                            dos.writeUTF("true");
                        }

                        running = false;

                    } else if (command.equals("/m") || command.equals("/u")) {
                        // Checks of the command is a regular message to send it directly
                        // back to the clients

                        //Calls for the send to client method
                        sendToClient(recieved);


                    } else if (command.equals("/a")) {

                        if (serverProtocol.onlineUsers.size() > counter) {

                            sendOnlineListToClient();
                            counter += 1;
                        }

                    } else if (command.equals("/w")) {

                        try {

                            int idOne = serverProtocol.getUserId(data.substring(0, 16));
                            int idTwo = serverProtocol.getUserId(data.substring(16, 32));

                            int roomID = serverProtocol.privateMessage(idOne, idTwo);

                            if (roomID != 0) {

                                String newRoomId = "01" + String.valueOf(serverProtocol.createRoomId());

                                sendToClient(recieved + newRoomId);

                            } else {

                                System.out.println(roomID);
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    } else if (command.equals("/0")) {

                        sendToClient("/8" + data.substring(5));


                        for (int i = 0; i < clientHandlers.size(); i++) {

                            //if (String.valueOf(c.s.getPort()).equals(data)){

                            if (String.valueOf(clientHandlers.get(i).s.getPort()).equals(data.substring(0,5))) {

                                for (int u = 0; u < serverProtocol.onlineUsers.size(); u++){

                                    if (serverProtocol.onlineUsers.get(u).equals(data.substring(5))){

                                        serverProtocol.onlineUsers.remove(u);
                                        break;
                                    }
                                }

                                serverProtocol.databaseProtocol(command, data.substring(5));
                                counter -= 1;
                                clientHandlers.get(i).s.close();
                                clientHandlers.remove(i);

                            }
                        }
                    } else if (command.equals("/f")) {

                        serverProtocol.forgotPassword(data);
                    }
                }
            } catch (EOFException eofE) {

                eofE.printStackTrace();

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }

}

