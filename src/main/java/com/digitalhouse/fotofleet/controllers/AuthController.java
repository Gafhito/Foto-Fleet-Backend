package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.LoginDto;
import com.digitalhouse.fotofleet.dtos.RegisterDto;
import com.digitalhouse.fotofleet.dtos.ResponseLoginDto;
import com.digitalhouse.fotofleet.dtos.UpdateRolDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.exceptions.ResponseException;
import com.digitalhouse.fotofleet.models.User;
import com.digitalhouse.fotofleet.services.AuthService;
import com.digitalhouse.fotofleet.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Permite el registro de usuarios y la autenticación de los mismos")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @Operation(summary = "Registrar Moderador", description = "Permite el registro de usuarios con un nivel de Moderador", responses = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Error al registrar usuario", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/register/moderator")
    public ResponseEntity<?> registerModerator(@RequestBody RegisterDto registerDto) throws BadRequestException {
        return new ResponseEntity<>(authService.register("Moderator", registerDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Registrar Usuario", description = "Permite el registro de usuarios con un nivel de privilegios mínimo", responses = {
            @ApiResponse(responseCode = "201", description = "Usuario creado con éxito", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Error al registrar usuario", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDto registerDto) throws BadRequestException {
        return new ResponseEntity<>(authService.register("User", registerDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Login de usuarios", description = "Permite el login de usuarios al sistema", responses = {
            @ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(schema = @Schema(implementation = ResponseLoginDto.class))),
            @ApiResponse(responseCode = "403", description = "Error al iniciar sesión")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(authService.login(loginDto), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar rol de usuarios", description = "Solo para usuarios administradores, permite actualizar el rol de usuarios moderadores o de bajo nivel", responses = {
            @ApiResponse(responseCode = "200", description = "Actualización de rol exitosa", content = @Content(schema = @Schema(implementation = UpdateRolDto.class))),
            @ApiResponse(responseCode = "400", description = "No se puede asignar rol Admin a un usuario", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para realizar dicha acción"),
            @ApiResponse(responseCode = "404", description = "No existe el rol que ha intentado asignar", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/update")
    public ResponseEntity<?> updateRol(@RequestBody UpdateRolDto updateRolDto) throws ResourceNotFoundException, BadRequestException {
        return new ResponseEntity<>(userService.updateRol(updateRolDto), HttpStatus.OK);
    }
}
