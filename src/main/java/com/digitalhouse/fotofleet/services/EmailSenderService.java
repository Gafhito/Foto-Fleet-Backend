package com.digitalhouse.fotofleet.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service

public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,
                          String subject,
                          String body){

        MimeMessage message = mailSender.createMimeMessage();

        try {
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("infofotofleet@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true);  // Establece el contenido como HTML

        mailSender.send(message);

        System.out.println("Mail Sent successfully....");
    } catch (MessagingException e) {
        // Manejo de excepciones
        e.printStackTrace();
        // Puedes agregar un manejo adecuado de excepciones aquí
    }
  }
}
