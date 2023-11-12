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

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterDto registerDto) throws BadRequestException {
        return new ResponseEntity<>(authService.register("Admin", registerDto), HttpStatus.CREATED);
    }

    @PostMapping("/register/moderator")
    public ResponseEntity<?> registerModerator(@RequestBody RegisterDto registerDto) throws BadRequestException {
        return new ResponseEntity<>(authService.register("Moderator", registerDto), HttpStatus.CREATED);
    }

    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) throws BadRequestException {
        return new ResponseEntity<>(authService.register("User", registerDto), HttpStatus.CREATED);
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
