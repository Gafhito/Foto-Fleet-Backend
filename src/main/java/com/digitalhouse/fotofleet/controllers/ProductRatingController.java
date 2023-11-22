package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.ProductRatingDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.exceptions.ResponseException;
import com.digitalhouse.fotofleet.services.ProductRatingService;
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

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
@Tag(name = "Ratings", description = "Permite a los usuarios puntuar los productos alquilados y hacer una review de los mismos")
public class ProductRatingController {
    private final ProductRatingService productRatingService;

    @Operation(summary = "Obtener listado de ratings", description = "Obtiene la lista de reviews de un producto en concreto", responses = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido con éxito", content = @Content(schema = @Schema(implementation = ProductRatingDto.class))),
            @ApiResponse(responseCode = "404", description = "No existe el producto o no tiene reviews aún", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductRating(@PathVariable Integer productId) throws ResourceNotFoundException {
        return new ResponseEntity<>(productRatingService.listRantingsByProductId(productId), HttpStatus.OK);
    }

    @Operation(summary = "Obtener listado de ratings", description = "Obtiene la lista de reviews de un producto en concreto", responses = {
            @ApiResponse(responseCode = "201", description = "Review creada con éxito", content = @Content(schema = @Schema(implementation = ProductRatingDto.class))),
            @ApiResponse(responseCode = "400", description = "No existe el producto con este ID", content = @Content(schema = @Schema(implementation = ResponseException.class))),
            @ApiResponse(responseCode = "404", description = "El usuario no tiene alquileres completos del producto seleccionado", content = @Content(schema = @Schema(implementation = ResponseException.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{productId}")
    public ResponseEntity<?> createRaview(@PathVariable Integer productId, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt, @RequestBody ProductRatingDto productRatingDto) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(productRatingService.createReview(jwt, productId, productRatingDto), HttpStatus.CREATED);
    }
}
