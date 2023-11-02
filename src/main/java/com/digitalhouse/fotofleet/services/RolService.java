package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.models.Rol;
import com.digitalhouse.fotofleet.repositories.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolService {
    private final RolRepository rolRepository;

    public Optional<Rol> getRolByName(String roleName) {
        return rolRepository.findByName(roleName);
    }
}
