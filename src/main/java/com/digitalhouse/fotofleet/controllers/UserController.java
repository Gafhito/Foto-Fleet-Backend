package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.services.EmailSenderService;
import com.digitalhouse.fotofleet.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @GetMapping
    public ResponseEntity<?> profile(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) throws BadRequestException {
        return new ResponseEntity<>(userService.getProfile(jwt), HttpStatus.OK);
    }

    @GetMapping("/resend")
    public ResponseEntity<?> resendEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) throws ResourceNotFoundException {
        emailSenderService.resendEmail(jwt);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
