package org.seng302.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import java.util.Properties;

/**
 * Service Class to set JavaMailSender props.
 */
@Service
public class MailSendingService {

    @Value("${support.email}")
    private String supportEmail;
    @Value("${support.password}")
    private String supportPassword;

    /**
     * A JavaMailSender bean that sets up SMTP details for the support email account.
     * To be used by an instance of EmailService.
     * @return
     */
    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(supportEmail);
        mailSender.setPassword(supportPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }



}
