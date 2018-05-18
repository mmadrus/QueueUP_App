package Client;

public class Data {

    private User user;

    private static Data ourInstance = new Data();

    public static Data getInstance() {
        return ourInstance;
    }

    private Data() {
    }

    protected void setUser (String username) {

        user = new User(username);
    }

    protected String getUser () {

        return user.getCurrentUser();
    }

}
