package org.seng302.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Concrete implementation of an email service. Currently sends a basic email with no attachments.
 */
@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${support.email}")
    private String supportEmail;

    /**
     * Sends a simple text only email to the given email address.
     * @param to A string containing the email address to send to
     * @param subject A string containing the email subject
     * @param text A string containing the email content
     */
    public void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(supportEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    /**
     * Sends a MimeMessage with the given HTML content to the given email address.
     * @param to A string containing the email address to send to
     * @param subject A string containing the email subject
     * @param html A string containing the raw HTML to send.
     * @throws MessagingException
     */
    public void sendHTMLMessage(String to, String subject, String html) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(supportEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        emailSender.send(message);
    }

}
