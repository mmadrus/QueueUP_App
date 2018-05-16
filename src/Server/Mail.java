package Server;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {

    public void sendEmail (String userEmail, String username, String password) {

        final String qupEmail = "qupchat@gmail.com";
        final String qupPassword = "QueueUp123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(qupEmail, qupPassword);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("qupchat@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(userEmail));
            message.setSubject("Your QUP!");
            message.setText("Here are your user details.\n\nUsername: " + username +
                    "\nPassword: " + password + "\n\nHope you enjoy your QUP! :)");

            Transport.send(message);

            System.out.println("Done");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

} //Source: https://www.mkyong.com/java/javamail-api-sending-email-via-gmail-smtp-example/
