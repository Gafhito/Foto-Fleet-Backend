package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.UserDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.exceptions.ResponseException;
import com.digitalhouse.fotofleet.services.EmailSenderService;
import com.digitalhouse.fotofleet.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Usuario", description = "Permite verificar la información del usuario del sistema")
public class UserController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @Operation(summary = "Perfil del usuario", description = "Obtiene el perfil del usuario en base a la petición enviada con el JWT en la cabecera de Authorization", responses = {
            @ApiResponse(responseCode = "200", description = "Perfil obtenido exitosamente", content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "No existe un usuario con este email", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<?> profile(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) throws BadRequestException {
        return new ResponseEntity<>(userService.getProfile(jwt), HttpStatus.OK);
    }

    @Operation(summary = "Reenvío de email de confirmación", description = "Reenvía el email de registro al usuario en base a la petición enviada con el JWT en la cabecera de Authorization", responses = {
            @ApiResponse(responseCode = "200", description = "Email reenviado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No existe un usuario con este email", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/resend")
    public ResponseEntity<?> resendEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) throws ResourceNotFoundException {
        emailSenderService.resendEmail(jwt);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
