package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.ProductRatingDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.services.ProductRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class ProductRatingController {
    private final ProductRatingService productRatingService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductRanting(@PathVariable Integer productId) throws ResourceNotFoundException {
        return new ResponseEntity<>(productRatingService.listRantingsByProductId(productId), HttpStatus.OK);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<?> createRaview(@PathVariable Integer productId, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt, @RequestBody ProductRatingDto productRatingDto) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(productRatingService.createReview(jwt, productId, productRatingDto), HttpStatus.CREATED);
    }
}
