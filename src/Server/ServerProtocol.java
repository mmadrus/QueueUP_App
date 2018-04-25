package Server;

import java.util.Random;

public class ServerProtocol {

    public boolean databaseProtocol(String command, String data) {

        boolean succesfull = true;

        if (command.equals("/1")) {
            try {

                int id = createUserId();

                Database db = new Database();

                boolean found = db.searchForUserEmail(data.substring(36));

                if (found == false) {

                    db.addUser(id, data.substring(0, 16), data.substring(16, 36), data.substring(36));


                    succesfull = true;

                } else {


                    succesfull = false;

                }

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

        System.out.println("success: " + succesfull);

        return succesfull;

    }

    public void chatProtocol(String command, Object data) {

    }

    public int createUserId() {


        Random random = new Random();
        String id = String.format("%04d", random.nextInt(10000));

        return Integer.parseInt(id);
    }

}