package Client;


public class Administrator extends User {

    private Boolean isAdmin;


    public Administrator(String username, String password, String email, Boolean isAdmin) {
        super(username, password, email);
        this.isAdmin = isAdmin;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }
}
