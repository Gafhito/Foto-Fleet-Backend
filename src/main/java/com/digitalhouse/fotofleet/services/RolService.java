package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.repositories.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolService {
    private final RolRepository rolRepository;
}
