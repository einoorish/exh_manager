package com.einoorish.exhibitionsbackend.service;

import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendApprovalMail(String emailTo, String generatedPassword){
        Email email = EmailBuilder.startingBlank()
                .from("Digital Archive", "digital.arhive.ua@noreply.com")
                .to(emailTo, emailTo)
                .withSubject("Application was approved")
                .withPlainText("We are pleased to inform you that your registration with The Digital Archive has been approved. \n" +
                        "\n" +
                        "Please use the following login details to access your account:\n" +
                        "\n" +
                        "Username: "+emailTo+"\n" +
                        "Password: "+generatedPassword+"\n" +
                        "\n" +
                        "Thank you for joining our community and we look forward to seeing your contributions!\n" +
                        "\n" +
                        "Best regards,\n" +
                        "The Digital Archive team")
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 465, "testingjavamail78@gmail.com", "nzolwvctcfiaeyjm")
                .withTransportStrategy(TransportStrategy.SMTPS)
                .buildMailer();

        mailer.sendMail(email);
    }

    public void sendRejectionMail(String emailTo) {
        Email email = EmailBuilder.startingBlank()
                .from("Digital Archive", "digital.arhive.ua@noreply.com")
                .to(emailTo, emailTo)
                .withSubject("Application was rejected")
                .withPlainText("We regret to inform you that your application to join The Digital Archive has been declined. Our verification process was unable to confirm your authority. It is possible that the information you provided was not up-to-date. " +
                        "\nHowever, we encourage you to submit a new application if you wish to reapply.\n" +
                        "\n" +
                        "Best regards,\n" +
                        "The Digital Archive team")
                .buildEmail();

        Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 465, "testingjavamail78@gmail.com", "nzolwvctcfiaeyjm")
                .withTransportStrategy(TransportStrategy.SMTPS)
                .buildMailer();

        mailer.sendMail(email);
    }
}
