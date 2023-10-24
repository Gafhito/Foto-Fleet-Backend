package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.repositories.RentalStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalStatusService {
    private final RentalStatusRepository rentalStatusRepository;
}
