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
}
