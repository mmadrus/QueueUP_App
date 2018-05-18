package Client;

import java.util.ArrayList;

public class Admin {

    private ArrayList<String> adminList = new ArrayList<>();

    public void addAdmin () {

        adminList.add("Matteo**********");
        adminList.add("johan***********");
        adminList.add("liridon*********");

    }

    public ArrayList<String> getAdminList() {
        return adminList;
    }
}
