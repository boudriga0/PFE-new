package com.reclamation.pfe.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.sql.DataSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MailSenderController {

    private static final String ENTITY_NAME = "Mail";

    @RequestMapping("/send-mail")
    public ResponseEntity<String> sendMail(@RequestBody EmailRequest request) throws InterruptedException {
        String response;

        final String username = "boudrigaahmed7@gmail.com";
        final String password = "yadcnvfymjwwskcr";
        String fromEmail = request.getFrom();
        List<String> toEmail = request.getTo(); // Corrected: Added generic type for List

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(
            properties,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            }
        );

        // Start our mail message
        //sleep(3000);
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(fromEmail));
            toEmail.forEach(email -> {
                try {
                    System.out.println(email.toString());
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email.toString()));
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });

            msg.setSubject(request.getTitle());

            Multipart emailContent = new MimeMultipart();

            // Text body part
            MimeBodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(request.getSubject());

            // Attachment body part.

            File file = new File("C:/PFE/test.txt");

            try (FileOutputStream fos = new FileOutputStream(file);) {
                String pdfAsArrayByte = request.getData();
                byte[] decoder = Base64.getDecoder().decode(pdfAsArrayByte);

                fos.write(decoder);
                System.out.println("PDF File Saved");
            } catch (Exception e) {
                e.printStackTrace();
            }

            MimeBodyPart pdfAttachment = new MimeBodyPart();

            pdfAttachment.attachFile("C:/PFE/test.txt");
            // Attach body parts
            try {
                msg.setFrom(new InternetAddress(username, "pfe"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            emailContent.addBodyPart(textBodyPart);
            emailContent.addBodyPart(pdfAttachment);

            // Attach multipart to message
            msg.setContent(emailContent);

            Transport.send(msg);

            response = "succes d'envoie";
        } catch (MessagingException e) {
            response = "echec d'envoie";
            throw new BadRequestAlertException(response, ENTITY_NAME, "echec d'envoie");
        } catch (IOException e) {
            response = "echec d'envoie";
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body(response);
    }
}
