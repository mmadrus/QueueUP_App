package Client;
import java.util.*;

public class User {

  private String currentUser;

  public User(String currentUser) {
    this.currentUser = currentUser;
  }

  public String getCurrentUser() {
    return currentUser;
  }
}
