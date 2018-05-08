package Client;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class User {

  ArrayList<String> userList=new ArrayList<>();


    public ArrayList<String> getUserList() {
        return userList;
    }
}
