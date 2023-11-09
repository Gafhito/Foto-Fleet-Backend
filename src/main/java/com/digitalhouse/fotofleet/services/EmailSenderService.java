package com.digitalhouse.fotofleet.services;


import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender mailSender;
    private final JwtGenerator jwtGenerator;
    private final UserService userService;

    public void enviarCorreo(String toEmail, String firstName, String lastName) {
        // Construye el asunto y el cuerpo del mail llamando al usuario
        String subject = "¡Bienvenido a nuestra aplicación!";

        // Construye el cuerpo del correo con un enlace HTML
        String body = "Hola " + firstName + " " + lastName + ",<br><br>"
                + "Te has registrado exitosamente en nuestra aplicación. ¡Bienvenido!<br><br>"
                + "Gracias por unirte a nosotros. Para acceder a la aplicación, haz clic en el siguiente enlace:<br>"
                + "<a href=\"http://1023c07-grupo3.s3-website-us-east-1.amazonaws.com/\">Acceder a la aplicación</a>";


        // Llama al metodo sendMail de EmailSenderService
        sendEmail(toEmail, subject, body);
    }

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

    public void resendEmail(String jwt) throws ResourceNotFoundException {
        String email = jwtGenerator.getEmailOfJwt(jwt.substring(7));

        Optional<User> u = userService.getUserByEmail(email);
        if (u.isEmpty()) throw new ResourceNotFoundException("No existe un usuario con este email");

        enviarCorreo(u.get().getEmail(), u.get().getFirstName(), u.get().getLastName());
    }
}
