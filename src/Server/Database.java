package Server;

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
    private String addUserUrl = "insert into qup.user(user_id, user_name, user_password, user_mail, isAdmin, isBlocked)" +
            " VALUES ( ?,?,?,?,?,?)";

    // Prepared statement to check for emails in the db
    private String searchForUserEmailUrl = "select user_mail from qup.user where user_mail = ?";

    // Prepared statement to check for users in the db
    private String userLoginUrl = "select user_name, user_password from qup.user where user_name = ? AND user_password = ?";

    // Prepared statement to check for username and usermail in db
    private String forgotPasswordUrl = "select user_name, user_mail from qup.user where user_name = ? AND user_mail = ?";

    // Prepared statement to check for username in db
    private String channelUserList = "select user_name from user";


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

    public boolean forgotPassword(String name, String email) {
        boolean exist = false;

        try (PreparedStatement statement = c.prepareStatement(forgotPasswordUrl)) {

            statement.setString(1, name);
            statement.setString(2, email);
            ResultSet resultSet = statement.executeQuery();

            resultSet.next();


            String names = resultSet.getString("user_name");
            String mail = resultSet.getString("user_mail");

            if (mail.equals(email) && names.equals(name)) {

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

        public void channelList () {
        }

}