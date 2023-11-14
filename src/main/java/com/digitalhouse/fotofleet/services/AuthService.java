package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.LoginDto;
import com.digitalhouse.fotofleet.dtos.RegisterDto;
import com.digitalhouse.fotofleet.dtos.ResponseLoginDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
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
    private final EmailSenderService emailSenderService;

    public User register(String roleName, RegisterDto registerDto) throws BadRequestException {
        if (userService.existUserByEmail(registerDto.email())) throw new BadRequestException("Este correo no puede ser utilizado, por favor intenta con otro");

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
        emailSenderService.enviarCorreo(registerDto.email(), registerDto.firstName(), registerDto.lastName());


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
