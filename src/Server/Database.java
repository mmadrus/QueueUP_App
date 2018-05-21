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

        // Prepared statement to add user into the db
        String addUserUrl = "insert into qup.user(user_id, user_name, user_password, user_mail, isAdmin, isBlocked, isOnline)" +
                " VALUES ( ?,?,?,?,?,?,?)";
        try (PreparedStatement statement = c.prepareStatement(addUserUrl)) {

            statement.setInt(1, id);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.setString(4, email);
            statement.setBoolean(5, false);
            statement.setBoolean(6, false);
            statement.setBoolean(7, false);

            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to check for emails in the db
    public boolean searchForUserEmail(String email) {

        boolean exist = false;

        // Prepared statement to check for emails in the db
        String searchForUserEmailUrl = "select user_mail from qup.user where user_mail = ?";

        try (PreparedStatement statement = c.prepareStatement(searchForUserEmailUrl)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            String mail = resultSet.getString("user_mail");

            if (mail.equals(email)) {

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

        // Prepared statement to check for users in the db
        String userLoginUrl = "select user_name, user_password from qup.user where user_name = ? AND user_password = ?";

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

    public void createPrivateRoom(String user1, String user2) {

        String findUserId = "select user_ID from qup.user where user_name = ?";

        try (PreparedStatement statement = c.prepareStatement(findUserId)) {

            statement.setString(1, user1);
            ResultSet resultSet1 = statement.executeQuery();
            resultSet1.next();
            String userId1 = resultSet1.getString("user_ID");

            statement.setString(1, user2);
            ResultSet resultSet2 = statement.executeQuery();
            resultSet2.next();
            String userId2 = resultSet2.getString("user_ID");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void userHasUser(String user1, String user2, int room) {

        String createPrivateChat = "insert into qup.user_has_user(user_user_ID1, user_user_ID2, pmessage_time, pmessage_id)" +
                " VAlUES(?,?,?,?)";

        try (PreparedStatement statement = c.prepareStatement(createPrivateChat)) {


            Timestamp time = new Timestamp(System.currentTimeMillis());

            //File file = new File("TEST.txt");
            //FileInputStream fi = new FileInputStream(file);

            statement.setString(1, user1);
            statement.setString(2, user2);
            statement.setTimestamp(3, time);
            statement.setInt(4, room);

            statement.execute();
            //Files.write(file,StandardOpenOption.CREATE,StandardOpenOption.APPEND);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean searchForUser(String username) {
        boolean exist = false;

        String userSearch = "Select user_name from user where user_name = ?";

        try (PreparedStatement statement = c.prepareStatement(userSearch)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            String user = resultSet.getString("user_name");

            if (user.equals(username)) {

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

        // Prepared statement to check for username and usermail in db
        String forgotPasswordUrl = "select user_password, user_mail from qup.user where user_name = ?";

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

    public void setUpdateStatusToOnline(String username) {

        // Update status to online
        String updateStatusToOnline = "update qup.user set isOnline = true where user_name = ?";

        try (PreparedStatement statement = c.prepareStatement(updateStatusToOnline)) {

            statement.setString(1, username);
            statement.execute();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void setUpdateStatusToOffline(String username) {


        // Update status to offline
        String updateStatusToOffline = "update qup.user set isOnline = false where user_name = ?";

        try (PreparedStatement statement = c.prepareStatement(updateStatusToOffline)) {

            statement.setString(1, username);
            statement.execute();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int setFindUserId(String username) {

        int userID;

        String findUserId = "select user_ID from qup.user where user_name = ?";

        try (PreparedStatement statement = c.prepareStatement(findUserId)) {

            statement.setString(1, username);
            statement.execute();

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            userID = resultSet.getInt("user_ID");


        } catch (Exception e) {

            userID = 0;
        }

        return userID;

    }

    public boolean searchForPrivateRoom(int idOne, int idTwo) throws SQLException {


        boolean k = true;

        String findPrivateRoom = "select pmessage_id from qup.user_has_user where user_user_ID1 = ? AND user_user_ID2 = ? AND pmessage_id like '01%'";

        try (PreparedStatement statement = c.prepareStatement(findPrivateRoom)) {

            statement.setInt(1, idOne);
            statement.setInt(2, idTwo);

            boolean roomFound = statement.execute();

            //String roomFound = resultSet.getString(1);

            if (!roomFound) {

                k = false;

            } else {

                k = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return k;

    }

    public int privateRoomID(int user1, int user2) {

        String privateRoomID = "select pmessage_id from user_has_user where user_user_ID1 = ? AND user_user_ID2 = ?";

        int pmessage_id = 0;

        try(PreparedStatement statement = c.prepareStatement(privateRoomID)){
            statement.setInt(1,user1);
            statement.setInt(2,user2);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            pmessage_id = resultSet.getInt(1);


        }catch (Exception e){
            e.printStackTrace();
        }

        return pmessage_id;
    }

    public boolean changePassword (String password, String username) {

        boolean changed = true;

        // Update status to online
        String changePasswordURL = "update qup.user set user_password = ? where user_name = ?";

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

    public void setBanUser (String user) {

        String banUser = "update qup.user set isBlocked = true where user_name = ?";

        try (PreparedStatement statement = c.prepareStatement(banUser)){

            statement.setString(1, user);
            statement.execute();


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public boolean isBLocked (String username) {

        boolean banned = true;

        String isUserBanned = "select isBlocked from qup.user where user_name = ?";

        try (PreparedStatement statement = c.prepareStatement(isUserBanned)) {


            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            boolean checkIsBanned = resultSet.getBoolean(1);


            if (checkIsBanned == false) {

                banned = false;

            } else {

                banned = true;
            }

        } catch (Exception e ) {

            e.printStackTrace();
        }

        return banned;
    }

    public boolean unbanUser (String username) {

        boolean banned = true;

        String unbanUserURL = "update qup.user set isBlocked = false where user_name = ?";

        try (PreparedStatement statement = c.prepareStatement(unbanUserURL)) {


            statement.setString(1, username);

            boolean checkIsBanned = statement.execute();


            if (checkIsBanned == true) {

                banned = false;

            } else {

                banned = true;
            }

        } catch (Exception e ) {

            e.printStackTrace();
        }

        return banned;
    }


}