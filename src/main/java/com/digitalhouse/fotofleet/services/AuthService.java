package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.LoginDto;
import com.digitalhouse.fotofleet.dtos.RegisterDto;
import com.digitalhouse.fotofleet.dtos.ResponseLoginDto;
import com.digitalhouse.fotofleet.models.Rol;
import com.digitalhouse.fotofleet.models.User;
import com.digitalhouse.fotofleet.security.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final UserService userService;
    private final RolService rolService;

     //Inyectando EmailSenderService
     @Autowired
     private EmailSenderService EmailSenderService;

    public void enviarCorreo(String toEmail, String firstName, String lastName) {
        // Construye el asunto y el cuerpo del mail llamando al usuario
        String subject = "¡Bienvenido a nuestra aplicación!";

        // Construye el cuerpo del correo con un enlace HTML
        String body = "Hola " + firstName + " " + lastName + ",<br><br>"
                + "Te has registrado exitosamente en nuestra aplicación. ¡Bienvenido!<br><br>"
                + "Gracias por unirte a nosotros. Para acceder a la aplicación, haz clic en el siguiente enlace:<br>"
                + "<a href=\"http://1023c07-grupo3.s3-website-us-east-1.amazonaws.com/\">Acceder a la aplicación</a>";


        // Llama al metodo sendMail de EmailSenderService
        EmailSenderService.sendEmail(toEmail, subject, body);
    }


    public User register(String roleName, RegisterDto registerDto) {
        User user = new User(
                registerDto.firstName(),
                registerDto.lastName(),
                registerDto.email(),
                passwordEncoder.encode(registerDto.password()),
                registerDto.address(),
                registerDto.phone());


        Rol rol = rolService.getRolByName(roleName).get();  // Validación para siguiente sprint
        user.setRoles(Collections.singletonList(rol));

        // Después de registrar al usuario, llama al método para enviar el correo personalizado
        enviarCorreo(registerDto.email(), registerDto.firstName(), registerDto.lastName());


        return userService.createUser(user);
    }

    public ResponseLoginDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        String rol = userService.getRolByUserEmail(loginDto.email());

        return new ResponseLoginDto(rol, token);
    }
}
