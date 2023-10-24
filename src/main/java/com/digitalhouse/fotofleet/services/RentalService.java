package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.repositories.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
}
