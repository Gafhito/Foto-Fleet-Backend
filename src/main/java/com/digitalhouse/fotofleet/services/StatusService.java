package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.repositories.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
}
