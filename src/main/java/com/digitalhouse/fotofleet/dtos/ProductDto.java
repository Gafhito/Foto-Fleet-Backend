package com.digitalhouse.fotofleet.dtos;

import java.util.List;

public record ProductDto(
        Integer productId,
        String name,
        String description,
        Integer categoryId,
        Double rentalPrice,
        Integer stock,
        String status,
        List<ImageDto> images
) {
    public ProductDto(Integer productId, String name, String description, Integer categoryId, Double rentalPrice, Integer stock) {
        this(productId, name, description, categoryId, rentalPrice, stock, null, null);
    }

    public ProductDto(Integer productId, String name, String description, Integer categoryId, Double rentalPrice, Integer stock, String status) {
        this(productId, name, description, categoryId, rentalPrice, stock, status, null);
    }
}
