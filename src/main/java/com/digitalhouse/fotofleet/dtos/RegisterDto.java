package com.digitalhouse.fotofleet.dtos;

public record RegisterDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String address,
        String phone
) {
}
