package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Databas {

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

    private int userId;
    private String userName;
    private String userPassword;
    private String userMail;
    private boolean isAdmin;
    private boolean isBlocked;

    public Databas() throws SQLException {

        try {
            c =(Connection) DriverManager.getConnection(url);
            System.out.println("Connected to database");
        } catch (SQLException ex) {
            System.out.println("Fail to connect to database");
        }

    }

    public void addUser(){

        try (PreparedStatement statement = c.prepareStatement(addUserUrl)){

            statement.setInt(1,userId);
            statement.setString(2,userName);
            statement.setString(3,userPassword);
            statement.setString(4,userMail);
            statement.setBoolean(5,isAdmin);
            statement.setBoolean(6,isBlocked);

            statement.execute();

            System.out.println("User added");

        }catch (Exception e){
            System.out.println("FAIL");
        }
    }

}
