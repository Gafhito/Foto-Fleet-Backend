package com.digitalhouse.fotofleet.dtos;

import com.digitalhouse.fotofleet.models.Characteristics;

import java.util.List;

public record ProductDto(
        String name,
        String description,
        Integer categoryId,
        Double rentalPrice,
        Integer stock,
        String status,
        List<ImageDto> images,
        List<Characteristics> characteristics
) {
    public ProductDto(String name, String description, Integer categoryId, Double rentalPrice, Integer stock) {
        this(name, description, categoryId, rentalPrice, stock, null, null, null);
    }

    public ProductDto(String name, String description, Integer categoryId, Double rentalPrice, Integer stock, String status) {
        this(name, description, categoryId, rentalPrice, stock, status, null, null);
    }
}
