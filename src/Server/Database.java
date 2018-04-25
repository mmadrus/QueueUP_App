package Server;

import java.sql.*;

public class Database {

    PreparedStatement statement = null;
    Connection c = null;
    private String url = "jdbc:mysql://den1.mysql5.gear.host/qup?user=qup&password=Sv3t8?CUfd!S";
    private String addRoom = "insert into qup.room(room_id, room_name, room_password, isPrivate)" +
            " VAlUES(?,?,?,?)";

    private int roomId;
    private String roomName;
    private String roomPassword;
    private boolean isPrivate;


    private String addUserUrl = "insert into qup.user(user_id, user_name, user_password, user_mail, isAdmin, isBlocked)" +
            " VALUES ( ?,?,?,?,?,?)";

    private String searchForUserEmailUrl = "select user_mail from qup.user where user_mail = ?";

    private String searchForUserURL = "select user_name, user_password from qup.user where user_name = ? AND user_password = ?";

    public Database() throws SQLException {

        try {
            c = (Connection) DriverManager.getConnection(url);
            System.out.println("Connected to database");
        } catch (SQLException ex) {
            System.out.println("Fail to connect to database");
        }

    }

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

    public boolean searchForUserEmail(String email) {

        boolean exist = false;

        try (PreparedStatement statement = c.prepareStatement(searchForUserEmailUrl)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("Test: " + resultSet.next());


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

    public boolean searchForUserEmail (String name, String password) {


        return true;
    }

}
