package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.LoginDto;
import com.digitalhouse.fotofleet.dtos.RegisterDto;
import com.digitalhouse.fotofleet.dtos.UpdateRolDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.User;
import com.digitalhouse.fotofleet.services.AuthService;
import com.digitalhouse.fotofleet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    private ResponseEntity<?> error() {
        Map<String, String> message = new HashMap<>();
        message.put("error", "Este correo no puede ser utilizado, por favor intenta con otro");
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterDto registerDto) {
        ResponseEntity<?> response;
        if (userService.existUserByEmail(registerDto.email())) {
            response = error();
        } else {
            User user = authService.register("Admin", registerDto);
            response = new ResponseEntity<>(user, HttpStatus.CREATED);
        }

        return response;
    }

    @PostMapping("/register/moderator")
    public ResponseEntity<?> registerModerator(@RequestBody RegisterDto registerDto) {
        ResponseEntity<?> response;
        if (userService.existUserByEmail(registerDto.email())) {
            response = error();
        } else {
            User user = authService.register("Moderator", registerDto);
            response = new ResponseEntity<>(user, HttpStatus.CREATED);
        }

        return response;
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
        ResponseEntity<?> response;
        if (userService.existUserByEmail(registerDto.email())) {
            response = error();
        } else {
            User user = authService.register("User", registerDto);
            response = new ResponseEntity<>(user, HttpStatus.CREATED);
        }

        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(authService.login(loginDto), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateRol(@RequestBody UpdateRolDto updateRolDto) throws ResourceNotFoundException, BadRequestException {
        return new ResponseEntity<>(userService.updateRol(updateRolDto), HttpStatus.OK);
    }
}
