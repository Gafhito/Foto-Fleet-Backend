package com.digitalhouse.fotofleet.services;


import com.digitalhouse.fotofleet.dtos.RentalDto;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Product;
import com.digitalhouse.fotofleet.models.User;
import com.digitalhouse.fotofleet.security.JwtGenerator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceRental {
    private final JavaMailSender mailSender;


    

    public void enviarCorreoRental(String toEmail, String firstName, String lastName, List<Product> products) {
        // Construye el asunto y el cuerpo del mail llamando al usuario
        String subject = "¡Has realizado una reserva en Fotofleet!";

        // Construye el cuerpo del correo con un enlace HTML
        String body = "Hola " + firstName + " " + lastName + ",<br><br>"
                + "Te comentamos que realizaste una reserva de los siguientes productos:<br><br>";
        for(Product p:products){
            body+= p.getName()+"<br>";

        }
        body+= "Muchas gracias por elegirnos";
        // Llama al metodo sendMail de EmailSenderServiceRental
        sendEmailRental(toEmail, subject, body);
    }

    public void sendEmailRental(String toEmail,
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