package Server;

import java.sql.*;

public class Database {

    PreparedStatement statement = null;
    Connection c = null;

    // Connection url for database
    private String url = "jdbc:mysql://den1.mysql5.gear.host/qup?user=qup&password=Sv3t8?CUfd!S";

    // Prepared statement to add room to the db
    private String addRoom = "insert into qup.room(room_id, room_name, room_password, isPrivate)" +
            " VAlUES(?,?,?,?)";

    private int roomId;
    private String roomName;
    private String roomPassword;
    private boolean isPrivate;

    // Prepared statement to add user into the db
    private String addUserUrl = "insert into qup.user(user_id, user_name, user_password, user_mail, isAdmin, isBlocked)" +
            " VALUES ( ?,?,?,?,?,?)";

    // Prepared statement to check for emails in the db
    private String searchForUserEmailUrl = "select user_mail from qup.user where user_mail = ?";

    // Prepared statement to check for users in the db
    private String userLoginUrl = "select user_name, user_password from qup.user where user_name = ? AND user_password = ?";

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
    public boolean userLogin (String name, String password) {

        boolean exist = false;

        try (PreparedStatement statement = c.prepareStatement(userLoginUrl)){

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

}
