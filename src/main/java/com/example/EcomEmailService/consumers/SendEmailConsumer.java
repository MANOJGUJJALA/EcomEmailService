package com.example.EcomEmailService.consumers;


import com.example.EcomEmailService.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ReceiveEmailDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class SendEmailConsumer {

    private ObjectMapper objectMapper;
    private EmailService emailService;

    public SendEmailConsumer(ObjectMapper objectMapper,EmailService emailService){
        this.objectMapper=objectMapper;
        this.emailService=emailService;
    }


    //This method should be called if we receive an event for sending an email(Sigup)
    //This method/consumer should register itself to the singUp topic.
    @KafkaListener(topics = "signUp",groupId = "emailService")
    public void handleSignUpEvent(String  message){

        //We are getting String message.
        //Convert this string message to object using ObjectMapper

        try{

        ReceiveEmailDto receiveEmailDto=objectMapper.readValue(message,ReceiveEmailDto.class);


            String smtpHostServer = "smtp.gmail.com";
            String emailID = "sampleid@gmail.com";
            final String password = "mypassword"; // correct password for gmail id



            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS


            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(receiveEmailDto.getFrom(), password);
                }
            };
            Session session = Session.getInstance(props, auth);

            emailService.sendEmail(session,receiveEmailDto.getTo(),receiveEmailDto.getSubject(),receiveEmailDto.getBody());

        }
        catch (Exception e){
            System.out.println("SomeThing Went Wroung");
        }


    }
}
