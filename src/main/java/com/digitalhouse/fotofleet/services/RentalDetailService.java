package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.repositories.RentalDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalDetailService {
    private final RentalDetailRepository rentalDetailRepository;
}
