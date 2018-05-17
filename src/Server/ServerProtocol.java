package Server;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class ServerProtocol {

    public ArrayList<String> onlineUsers = new ArrayList<>();

    // Method for the commands that have something to do with the db, takes the command as a parameter and the rest as another
    // returns a boolean which is later sent to the client as a string
    public boolean databaseProtocol(String command, String data) {

        boolean succesfull = true;
        String nuFunkarDet;
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

                    // Create a new string builder to later save the user name in
                    StringBuilder finalUser = new StringBuilder();

                    // For loop to convert the padded username returned from the server into a username without pads
                    for (int p = 0; p < data.substring(0,16).length(); p++) {

                        // Removes the * form the returned username, keeps the letters and numbers and then
                        // saves them into the StringBuilder finalUser
                        if (!String.valueOf(data.substring(0,16).charAt(p)).equals("*")) {

                            finalUser.append(String.valueOf(data.substring(0,16).charAt(p)));
                        }


                    }

                    // Create a new string builder to later save the user name in
                    StringBuilder finalPassword = new StringBuilder();

                    // For loop to convert the padded username returned from the server into a username without pads
                    for (int p = 0; p < data.substring(16,36).length(); p++) {

                        // Removes the * form the returned username, keeps the letters and numbers and then
                        // saves them into the StringBuilder finalUser
                        if (!String.valueOf(data.substring(16,36).charAt(p)).equals("*")) {

                            finalPassword.append(String.valueOf(data.substring(16,36).charAt(p)));
                        }


                    }

                    sendEmail(data.substring(36), String.valueOf(finalUser), String.valueOf(finalPassword));
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

                        succesfull = true;

                    }

                } catch (Exception e) {

                    e.printStackTrace();

                }
        } else if (command.equals("/w")){

            try {

                Database db = new Database();

                boolean founduser1 = db.searchForUser(data.substring(0,16));
                boolean founduser2 = db.searchForUser(data.substring(16));

                //System.out.println(data.substring(0,16));

                // Returns a boolean whether or not the username and password is found
                if (founduser1 && founduser2 == false) {

                    succesfull = false;

                } else {

                    db.createPrivateRoom(data.substring(0,16), data.substring(16));
                    System.out.println("kollar om denna körs");

                    succesfull = true;

                }

                System.out.println(succesfull);

            } catch (Exception e){
                e.printStackTrace();
            }
        } else if (command.equals("/0")){

            try {

                Database db = new Database();


                db.setUpdateStatusToOffline(data.substring(0,16));

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return succesfull;

    }

    public int getUserId (String u) {

        int i = 0;

        try {

            Database db = new Database();

            i = db.setFindUserId(u);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return i;
    }

    /*public int getRoomID (int idOne, int idTwo) {

        int i = 0;

        try {

            Database db = new Database();

            i = db.searchForPrivateRoom(idOne, idTwo);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return i;
    }*/

    //Method to create user id
    public int createUserId() {


        Random random = new Random();
        String id = String.format("%04d", random.nextInt(10000));

        return Integer.parseInt(id);
    }

    public int createRoomId () {

        Random random = new Random();
        String id = String.format("%08d", random.nextInt(100000000));

        return Integer.parseInt(id);
    }

    public int privateMessage (int idOne, int idTwo) throws SQLException {

        int id = 0;

        try {

            Database db = new Database();

            boolean found = db.searchForPrivateRoom(idOne, idTwo);

            if (found == true) {

                db.userHasUser(String.valueOf(idOne),String.valueOf(idTwo),createRoomId());

                id = 1;


            } else if (found == false){

                id = 0;
            }

        } catch (SQLException e){
            System.out.println(" Körs denna eller ? ");
        } catch (Exception e) {

            e.printStackTrace();
        }

        return id;

    }

    public void sendEmail (String userEmail, String username, String password) {

        try {

            //Database db = new Database();

            /*String username = db.getUsername(userEmail);
            String password = db.getUserPassword(userEmail);*/

            Mail mail = new Mail();

            mail.sendEmail(userEmail, username, password);

            System.out.println("hejsan");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forgotPassword (String data) {

        try {

            Database db = new Database();

            String userDetails = db.forgotPassword(data);

            // Create a new string builder to later save the user name in
            StringBuilder finalUser = new StringBuilder();

            // For loop to convert the padded username returned from the server into a username without pads
            for (int p = 0; p < userDetails.substring(0,16).length(); p++) {

                // Removes the * form the returned username, keeps the letters and numbers and then
                // saves them into the StringBuilder finalUser
                if (!String.valueOf(userDetails.substring(0,16).charAt(p)).equals("*")) {

                    finalUser.append(String.valueOf(userDetails.substring(0,16).charAt(p)));
                }


            }

            // Create a new string builder to later save the user name in
            StringBuilder finalPassword = new StringBuilder();

            // For loop to convert the padded username returned from the server into a username without pads
            for (int p = 0; p < userDetails.substring(16,36).length(); p++) {

                // Removes the * form the returned username, keeps the letters and numbers and then
                // saves them into the StringBuilder finalUser
                if (!String.valueOf(userDetails.substring(16,36).charAt(p)).equals("*")) {

                    finalPassword.append(String.valueOf(userDetails.substring(16,36).charAt(p)));
                }


            }

            sendEmail(userDetails.substring(36), String.valueOf(finalUser), String.valueOf(finalPassword));


        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
