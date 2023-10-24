package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.repositories.RentalProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalProductService {
    private final RentalProductRepository rentalProductRepository;
}
