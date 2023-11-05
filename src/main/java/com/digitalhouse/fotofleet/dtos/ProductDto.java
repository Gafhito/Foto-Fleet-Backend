package com.digitalhouse.fotofleet.dtos;

import java.util.List;

public record ProductDto(
        String name,
        String description,
        Integer categoryId,
        Double rentalPrice,
        Integer stock,
        String statusId,
        List<ImageDto> images
) {
    public ProductDto(String name, String description, Integer categoryId, Double rentalPrice, Integer stock) {
        this(name, description, categoryId, rentalPrice, stock, null, null);
    }

    public ProductDto(String name, String description, Integer categoryId, Double rentalPrice, Integer stock, String statusId) {
        this(name, description, categoryId, rentalPrice, stock, statusId, null);
    }
}
