package com.digitalhouse.fotofleet.dtos;

public record ProductDto(
        String name,
        String description,
        Double rentalPrice,
        Integer stock,
        Integer categoryId
) {
}
