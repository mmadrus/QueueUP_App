package Server;

import Client.DataStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class ServerProtocol {

    public ArrayList<String> onlineUsers = new ArrayList<>();

    // Method for the commands that have something to do with the db, takes the command as a parameter and the rest as another
    // returns a boolean which is later sent to the client as a string
    public boolean databaseProtocol(String command, String data) {

        boolean succesfull = true;

        // Add user command
        if (command.equals("/1")) {

            try {

                //Creates an user id
                int id = createUserId();

                // Connects to the db
                Database db = new Database();

                //Returns true or false if the email exists in the db
                //The email starts at the 36th place in the string called data
                boolean found = db.searchForUserEmail(data.substring(36));

                //If the email does not exist it adds the user and returns the boolean as true
                if (found == false) {

                    //Calls for add user method in the db class
                    // Username starts index 0 and ends at 16, password starts at 16 and ends at 36 and email starts at 36
                    db.addUser(id, data.substring(0, 16), data.substring(16, 36), data.substring(36));


                    succesfull = true;

                } else { // else returns the boolean as false because the email already exists


                    succesfull = false;

                }

            } catch (Exception e) {

                e.printStackTrace();
            }

            // Command to look for username and its corresponding password
        } else if (command.equals("/6")) {

                try {

                    Database db = new Database();

                    // Same logic applies here as in the previous command
                    boolean foundUser = db.userLogin(data.substring(0,16), data.substring(16, 36));

                    // Just a check if the method in the db is working
                    System.out.println(data.substring(0,16) + "\n" + data.substring(16,36));

                    // Returns a boolean whether or not the username and password is found
                    if (foundUser == false) {

                        succesfull = false;

                    } else {

                        db.setUpdateStatusToOnline(data.substring(0,16));
                        onlineUsers.add(data.substring(0,16));

                        System.out.println(onlineUsers.size());

                        succesfull = true;

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }


        } else if (command.equals("/7")) {

            try {

                Database db = new Database();

                boolean found = db.searchForUsername(data.substring(0,16));

                if (found == true){

                    succesfull = true;

                } else {

                    succesfull = false;

                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }


        // Checks that the booleans are correct
        System.out.println("success: " + succesfull);

        return succesfull;

    }

    //Method to create user id
    public int createUserId() {


        Random random = new Random();
        String id = String.format("%04d", random.nextInt(10000));

        return Integer.parseInt(id);
    }

}
