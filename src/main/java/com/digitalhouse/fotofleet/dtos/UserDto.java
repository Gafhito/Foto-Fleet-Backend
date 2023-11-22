package com.digitalhouse.fotofleet.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto (
        String firstName,
        String lastName,
        String email,
        String address,
        String phone,
        LocalDateTime registrationDate,
        List<ProductDto> favorites
) {
    public UserDto(String firstName, String lastName, String email) {
        this(firstName, lastName, email, null, null, null, null);
    }
}
