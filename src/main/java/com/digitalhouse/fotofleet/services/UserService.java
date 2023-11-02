package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.models.Rol;
import com.digitalhouse.fotofleet.models.User;
import com.digitalhouse.fotofleet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Boolean existUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public String getRolByUserEmail(String email) {
        return userRepository.findUserRolesByEmail(email).get(0).getRoleName();
    }
}
