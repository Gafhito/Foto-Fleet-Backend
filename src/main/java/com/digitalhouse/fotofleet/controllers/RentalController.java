package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.RentalDto;
import com.digitalhouse.fotofleet.dtos.RentalResponseDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.exceptions.ResponseException;
import com.digitalhouse.fotofleet.services.EmailSenderServiceRental;
import com.digitalhouse.fotofleet.services.RentalService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
@Tag(name = "Alquileres", description = "Permite a los usuarios alquilar productos de la página")
public class RentalController {
    private final RentalService rentalService;
    private final EmailSenderServiceRental emailSenderServiceRental;

    @Operation(summary = "Añadir un alquiler", description = "Agrega un carrito de alquiler al usuario en base a la petición enviada con el JWT en la cabecera de Authorization", responses = {
            @ApiResponse(responseCode = "201", description = "Petición creada exitosamente", content = @Content(schema = @Schema(implementation = RentalResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Error en alguno de los productos del carrito de compra", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró algún producto del carrito", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<?> addRental(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt, @RequestBody List<RentalDto> rentalDtos) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(rentalService.addRentals(jwt, rentalDtos), HttpStatus.CREATED);
    }

    @Operation(summary = "Cambiar estado de alquiler a Activo", description = "Permite a un administrador cambiar el estado de alquiler de un producto en concreto a Activo", responses = {
            @ApiResponse(responseCode = "201", description = "Petición creada exitosamente", content = @Content(schema = @Schema(implementation = RentalResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró el producto", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/active")
    public ResponseEntity<?> statusActive(@RequestParam Integer rentalId) throws ResourceNotFoundException {
        return new ResponseEntity<>(rentalService.changeStatus(rentalId, "Active"), HttpStatus.OK);
    }

    @Operation(summary = "Cambiar estado de alquiler a Completo", description = "Permite a un administrador cambiar el estado de alquiler de un producto en concreto a Completo cuando lo devuelve el cliente", responses = {
            @ApiResponse(responseCode = "201", description = "Petición creada exitosamente", content = @Content(schema = @Schema(implementation = RentalResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró el producto", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/completed")
    public ResponseEntity<?> statusCompleted(@RequestParam Integer rentalId) throws ResourceNotFoundException {
        return new ResponseEntity<>(rentalService.changeStatus(rentalId, "Completed"), HttpStatus.OK);
    }
}
