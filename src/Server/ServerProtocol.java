package Server;

import Client.DataStream;

import javax.xml.crypto.Data;
import java.awt.image.DataBuffer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class ServerProtocol {

    public ArrayList<String> onlineUsers = new ArrayList<>();
    public ArrayList<String> tabs = new ArrayList<>();
    private int roomID = 1000000003;
    private int privateRoomID = 1200000001;

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


                // Returns a boolean whether or not the username and password is found
                if (founduser1 && founduser2 == false) {

                    succesfull = false;

                } else {

                    db.createPrivateRoom(data.substring(0,16), data.substring(16));

                    succesfull = true;

                }

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
        } else if (command.equals("/c")) {

            try {

                Database db = new Database();

                boolean changed = db.changePassword(data.substring(16), data.substring(0, 16));
                if (changed == true) {

                    succesfull = true;

                } else {

                    succesfull = false;
                }


            } catch (Exception e) {

                e.printStackTrace();
            }
        } else if (command.equals("/k")) {

            try {

                Database db = new Database();

                succesfull = db.isBLocked(data);

            } catch (Exception e) {

                e.printStackTrace();
            }
        } else if (command.equals("/4")) {

            try {

                Database db = new Database();

                succesfull = db.unbanUser(data);

            } catch (Exception e) {

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

    //Method to create user id
    public int createUserId() {


        Random random = new Random();
        String id = String.format("%04d", random.nextInt(10000));

        return Integer.parseInt(id);
    }

    public int createRoomId () {

        Random random = new Random();
        String id = "11"+String.format("%08d", random.nextInt(100000000));
        int nana = Integer.parseInt(id);
        return nana;
    }

    public int privateMessage (int idOne, int idTwo, int pmessageRoomID) throws SQLException {

        int id = 0;

        try {

            Database db = new Database();

            boolean found = db.searchForPrivateRoom(idOne, idTwo);

            if (found == false) {

                db.userHasUser(String.valueOf(idOne),String.valueOf(idTwo),pmessageRoomID);

                id = 1;


            } else if (found == true){


                id = 0;
            }

        } catch (SQLException e){
            e.printStackTrace();

        }

        return id;

    }
    public int getProomID(int idOne, int idTwo){

        int id = 0;

        try{
            Database db = new Database();


            id = db.privateRoomID(idOne,idTwo);

        }catch (Exception e){
            e.printStackTrace();
        }
        return id;
    }

    public void sendEmail (String userEmail, String username, String password) {

        try {


            Mail mail = new Mail();

            mail.sendEmail(userEmail, username, password);


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

    public void banUser(String user) {


        try {

            Database db = new Database();

            db.setBanUser(user);


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public String getRoomID() {
        return String.valueOf(roomID);
    }

    public void setRoomID(int i) {
        this.roomID += 1;
    }

    public String getPrivateRoomID() {
        return String.valueOf(privateRoomID);
    }

    public void setPrivateRoomIDRoomID(int i) {
        this.privateRoomID += 1;
    }

    public void createDM (int idOne, int idTwo, int roomID) {

        try {

            Database db = new Database();

            db.userHasUser(String.valueOf(idOne),String.valueOf(idOne),roomID);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
