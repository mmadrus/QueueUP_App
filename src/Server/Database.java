package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    private String url = "jdbc:mysql://den1.mysql5.gear.host/qup?user=qup&password=Sv3t8?CUfd!S";


    PreparedStatement statement = null;
    Connection c = null;

    private String addRoom = "insert into qup.room(room_id, room_name, room_password, isPrivate)"+
            " VAlUES(?,?,?,?)";

    private int roomId;
    private String roomName;
    private String roomPassword;
    private boolean isPrivate;


    private String addUserUrl =  "insert into qup.user(user_id, user_name, user_password, user_mail, isAdmin, isBlocked)" +
            " VALUES ( ?,?,?,?,?,?)";

    private String searchForUserEmailUrl = "select user_mail from qup.user where user_mail = ?";

    public Database() throws SQLException {

        try {
            c =(Connection) DriverManager.getConnection(url);
            System.out.println("Connected to database");
        } catch (SQLException ex) {
            System.out.println("Fail to connect to database");
        }

    }

    public void addUser(int id, String username, String password, String email){

        try (PreparedStatement statement = c.prepareStatement(addUserUrl)){

            statement.setInt(1,id);
            statement.setString(2,username);
            statement.setString(3,password);
            statement.setString(4,email);
            statement.setBoolean(5,false);
            statement.setBoolean(6,false);

            statement.execute();

            System.out.println("User added");

        }catch (Exception e){
            System.out.println("FAIL");
        }
    }

    public boolean searchForUserEmail (String email) {

        boolean exist = false;

        try (PreparedStatement statement = c.prepareStatement(searchForUserEmailUrl)) {

            statement.setString(1, email);
            statement.execute();

            if (email != null){

                exist = true;
                System.out.println("User exists");

            } else {

                exist = false;
                System.out.println("No user found");
            }


        } catch (Exception e) {

            e.printStackTrace();
        }

        return exist;

    }

}
