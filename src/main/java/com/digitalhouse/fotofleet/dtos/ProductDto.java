package com.digitalhouse.fotofleet.dtos;

public record ProductDto(
        String name,
        String description,
        Integer categoryId,
        Double rentalPrice,
        Integer stock,

        String statusId
) {
}
