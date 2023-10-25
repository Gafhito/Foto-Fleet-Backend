package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.models.User;
import com.digitalhouse.fotofleet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
