package com.digitalhouse.fotofleet.dtos;

import java.time.LocalDateTime;

public record UserDto (
        String firstName,
        String lastName,
        String email,
        String address,
        String phone,
        LocalDateTime registrationDate
) {
}
