package com.digitalhouse.fotofleet.controllers;

import com.digitalhouse.fotofleet.dtos.RentalDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.services.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<?> addRental(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt, @RequestBody List<RentalDto> rentalDtos) throws BadRequestException, ResourceNotFoundException {
        return new ResponseEntity<>(rentalService.addRentals(jwt, rentalDtos), HttpStatus.CREATED);
    }

    @PostMapping("/status")
    public ResponseEntity<?> changeStatus(@RequestParam Integer rentalId, @RequestParam String status) throws ResourceNotFoundException {
        return new ResponseEntity<>(rentalService.changeStatus(rentalId, status), HttpStatus.OK);
    }
}
