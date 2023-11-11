package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.repositories.CharacteristicsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacteristicsService {

    private final CharacteristicsRepository characteristicsRepository;
}
