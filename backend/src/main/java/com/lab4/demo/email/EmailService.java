package com.lab4.demo.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@RequiredArgsConstructor
@Service
public class EmailService {
    public void sendEmail(String email) throws MessagingException {

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("musicplayerlibrary@gmail.com", "#123Password");
            }
        });

        Message msg = new MimeMessage(session);

        msg.setSubject("Welcome!");
        msg.setContent("Thank you for choosing the best music player ever! \n\nYou can listen to music, create playlists and buy tracks.\n\nEnjoy!\n\n" ,"text/html");
        msg.setFrom(new InternetAddress("musicplayerlibrary@gmail.com", false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

        msg.setSentDate(new Date());

        Transport.send(msg);
    }

    public void sendScheduledEmail(String email) throws MessagingException {

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("musicplayerlibrary@gmail.com", "#123Password");
            }
        });

        Message msg = new MimeMessage(session);

        msg.setSubject("Welcome!");
        msg.setContent("Don't forget to buy songs and give us money , peasant\n\nEnjoy our services☺!\n\n" ,"text/html");
        msg.setFrom(new InternetAddress("musicplayerlibrary@gmail.com", false));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

        msg.setSentDate(new Date());

        Transport.send(msg);
    }

}