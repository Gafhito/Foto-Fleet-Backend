package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.models.Status;
import com.digitalhouse.fotofleet.repositories.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;

    public Optional<Status> getStatusByName(String name) {
        return statusRepository.findByName(name);
    }
}
