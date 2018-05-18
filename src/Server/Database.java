package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    private PreparedStatement statement = null;
    private Connection c = null;

    // Connection url for database
    private String url = "jdbc:mysql://den1.mysql5.gear.host/qup?user=qup&password=Sv3t8?CUfd!S";

    // Prepared statement to add room to the db
    private String addRoomUrl = "insert into qup.room(room_id, room_name, room_password, isPrivate)" +
            " VAlUES(?,?,?,?)";
    // Prepared statement to check if room exist
    private String searchForRoomUrl = "select room_name from qup.room where room_name = ?";

    // Prepared statement to add user into the db
    private String addUserUrl = "insert into qup.user(user_id, user_name, user_password, user_mail, isAdmin, isBlocked, isOnline)" +
            " VALUES ( ?,?,?,?,?,?,?)";

    // Prepared statement to check for emails in the db
    private String searchForUserEmailUrl = "select user_mail from qup.user where user_mail = ?";

    // Prepared statement to check for users in the db
    private String userLoginUrl = "select user_name, user_password from qup.user where user_name = ? AND user_password = ?";

    // Prepared statement to check for username and usermail in db
    private String forgotPasswordUrl = "select user_password, user_mail from qup.user where user_name = ?";

    // Prepared statement to check for username in db
    private String channelUserList = "select user_name from user";

    private String userSearch = "Select user_name from user where user_name = ?";

    // Update status to online
    private String updateStatusToOnline = "update qup.user set isOnline = true where user_name = ?";

    // Update status to offline
    private String updateStatusToOffline = "update qup.user set isOnline = false where user_name = ?";

    private String createPrivateChat = "insert into qup.user_has_user(user_user_ID1, user_user_ID2, pmessage_time, pmessage_id)" +
            " VAlUES(?,?,?,?)";

    private String findUserId = "select user_ID from qup.user where user_name = ?";

    private String findPrivateRoom = "select pmessage_id from qup.user_has_user where user_user_ID1 = ? AND user_user_ID2 = ? AND pmessage_id like '01%'";

    private String getUsernameURL = "select user_name from qup.user where user_mail = ?";

    private String getPasswordURL = "select user_password from qup.user where user_mail = ?";

    // Update status to online
    private String changePasswordURL = "update qup.user set user_password = ? where user_name = ?";


    // database constructor
    public Database() throws SQLException {

        try {
            c = (Connection) DriverManager.getConnection(url);
            System.out.println("Connected to database");
        } catch (SQLException ex) {
            System.out.println("Fail to connect to database");
        }

    }

    // Method to add user to the db
    public void addUser(int id, String username, String password, String email) {

        try (PreparedStatement statement = c.prepareStatement(addUserUrl)) {

            statement.setInt(1, id);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, email);
            statement.setBoolean(5, false);
            statement.setBoolean(6, false);
            statement.setBoolean(7, false);

            statement.execute();

            System.out.println("User added");

        } catch (Exception e) {
            System.out.println("FAIL");
        }
    }

    // Method to check for emails in the db
    public boolean searchForUserEmail(String email) {

        boolean exist = false;

        try (PreparedStatement statement = c.prepareStatement(searchForUserEmailUrl)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            String mail = resultSet.getString("user_mail");

            if (mail.equals(email)) {

                System.out.println(mail);
                exist = true;

            } else {

                exist = false;
            }


        } catch (Exception e) {

            e.getSuppressed();
        }


        return exist;

    }

    // Method to check for usernames with their corresponding passwords
    public boolean userLogin(String name, String password) {

        boolean exist = false;

        try (PreparedStatement statement = c.prepareStatement(userLoginUrl)) {

            statement.setString(1, name);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            String username = resultSet.getString("user_name");
            String pw = resultSet.getString("user_password");

            if (username.equals(name) && pw.equals(password)) {

                exist = true;

            } else {

                exist = false;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return exist;
    }

    // Method to update user textArea
    public ArrayList<String> userList() {
        ArrayList<String> userList = new ArrayList<>();
        try (PreparedStatement statement = c.prepareStatement(channelUserList)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                userList.add(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    // A method to add rooms to a database
    public void addRoom(int id, String roomName, String roomPassword, Boolean isPrivate) {
        try (PreparedStatement statement = c.prepareStatement(addRoomUrl)) {

            statement.setInt(1, id);
            statement.setString(2, roomName);
            statement.setString(3, roomPassword);
            statement.setBoolean(4, false);

            statement.execute();

            System.out.println("Room added");

        } catch (Exception e) {
            System.out.println("FAIL");
        }

    }

    // A method to search for room name in db
    public boolean searchForRoom(String roomName) {

        boolean exist = false;

        try (PreparedStatement statement = c.prepareStatement(searchForRoomUrl)) {

            statement.setString(1, roomName);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();


            String room = resultSet.getString("room_name");

            if (room.equals(roomName)) {

                System.out.println(room);
                exist = true;

            } else {

                exist = false;
            }


        } catch (Exception e) {

            e.getSuppressed();
        }


        return exist;

    }

    public void createPrivateRoom(String user1, String user2) {

        try (PreparedStatement statement = c.prepareStatement(findUserId)) {

            statement.setString(1, user1);
            ResultSet resultSet1 = statement.executeQuery();
            resultSet1.next();
            String userId1 = resultSet1.getString("user_ID");

            statement.setString(1, user2);
            ResultSet resultSet2 = statement.executeQuery();
            resultSet2.next();
            String userId2 = resultSet2.getString("user_ID");

//                PreparedStatement statement1 = c.prepareStatement(createPrivateChat);
//
//                statement1.setString(1, userId1);
//                statement1.setString(2, userId2);
//                ResultSet resultSet3 = statement1.executeQuery();
//                resultSet3.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void userHasUser(String user1, String user2, int room) {
        try (PreparedStatement statement = c.prepareStatement(createPrivateChat)) {


            Timestamp time = new Timestamp(System.currentTimeMillis());

            //File file = new File("TEST.txt");
            //FileInputStream fi = new FileInputStream(file);

            statement.setString(1, user1);
            System.out.println("Databas metod för att skapa privat rum anv1 = " + user1);
            statement.setString(2, user2);
            System.out.println("Databas metod för att skapa privat rum anv2 = " + user2);
            statement.setTimestamp(3, time);
            System.out.println("Databas metod för att skapa privat rum tid =" + time);
            //statement.setAsciiStream(4, fi, (int) file.length());
            statement.setInt(4, room);
            System.out.println("Databas metod för att skapa privat rum rumID =" + room);
            statement.execute();
            //Files.write(file,StandardOpenOption.CREATE,StandardOpenOption.APPEND);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean searchForUser(String username) {
        boolean exist = false;

        try (PreparedStatement statement = c.prepareStatement(userSearch)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            String user = resultSet.getString("user_name");

            if (user.equals(username)) {

                System.out.println(user);
                exist = true;

            } else {

                exist = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return exist;
    }

    public String forgotPassword(String name) {

        String userDetails = null;

        try (PreparedStatement statement = c.prepareStatement(forgotPasswordUrl)) {

            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();


            String password = resultSet.getString("user_password");
            String mail = resultSet.getString("user_mail");

            userDetails = name + password + mail;

        } catch (Exception e) {

            e.getSuppressed();

        }

        return userDetails;
    }

    public void channelList() {
    }

    public void setUpdateStatusToOnline(String username) {

        try (PreparedStatement statement = c.prepareStatement(updateStatusToOnline)) {

            statement.setString(1, username);
            statement.execute();

            System.out.println("Status set to online");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Not set as online");
        }

    }

    public void setUpdateStatusToOffline(String username) {

        try (PreparedStatement statement = c.prepareStatement(updateStatusToOffline)) {

            statement.setString(1, username);
            statement.execute();

            System.out.println("Status set to online");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Not set as online");
        }

    }

    public int setFindUserId(String username) {

        int userID;

        try (PreparedStatement statement = c.prepareStatement(findUserId)) {

           // statement.setBoolean(1, true);
            statement.setString(1, username);
            statement.execute();

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            userID = resultSet.getInt("user_ID");

           // userID = Integer.parseInt(user);

            System.out.println("ID: " + userID);

        } catch (Exception e) {

            userID = 0;
            System.out.println("ID: " + userID);
        }

        return userID;

    }

    public boolean searchForPrivateRoom(int idOne, int idTwo) throws SQLException {

        int id = 0;
        String test = "";
        boolean k = true;

        try (PreparedStatement statement = c.prepareStatement(findPrivateRoom)) {

            statement.setInt(1, idOne);
            statement.setInt(2, idTwo);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String roomFound = resultSet.getString(1);

            if (roomFound.isEmpty()) {

                k = false;

            } else {

                /*ResultSet rs = statement.executeQuery();
                String reslust = rs.getString(1);
                id = Integer.parseInt(reslust);
                System.out.println("Nej");*/

                k = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return k;
    }

    public String getUsername (String email) {

        String user = null;

        try (PreparedStatement statement = c.prepareStatement(getUsernameURL)){

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            user = resultSet.getString("user_name");

        } catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public String getUserPassword (String email) {

        String password = null;

        try (PreparedStatement statement = c.prepareStatement(getPasswordURL)){

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            password = resultSet.getString("user_password");

        } catch (Exception e){
            e.printStackTrace();
        }
        return password;
    }

    public boolean changePassword (String password, String username) {

        boolean changed = true;

        try (PreparedStatement statement = c.prepareStatement(changePasswordURL)) {


            statement.setString(1, password);
            statement.setString(2, username);

            int i = statement.executeUpdate();

            if (i == 1) {

                changed = true;

            } else if (i != 1) {

                changed = false;
            }
        } catch (Exception e ) {

            e.printStackTrace();
        }

        return changed;
    }


}