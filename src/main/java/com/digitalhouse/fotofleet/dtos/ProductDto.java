package com.digitalhouse.fotofleet.dtos;

import com.digitalhouse.fotofleet.models.Characteristics;

import java.util.List;

public record ProductDto(
        Integer productId,
        String name,
        String description,
        Integer categoryId,
        Double rentalPrice,
        Integer stock,
        String status,
        List<ImageDto> images,
        List<Characteristics> characteristics,
        List<RentalDateDto> rentalDate
) {
    public ProductDto(Integer productId, String name, String description, Integer categoryId, Double rentalPrice, Integer stock) {
        this(productId, name, description, categoryId, rentalPrice, stock, null, null, null, null);
    }

    public ProductDto(Integer productId, String name, String description, Integer categoryId, Double rentalPrice, Integer stock, String status) {
        this(productId, name, description, categoryId, rentalPrice, stock, status, null, null, null);
    }


    public ProductDto(Integer productId, String name, String description, Integer categoryId, Double rentalPrice, Integer stock, String status, List<ImageDto> images, List<Characteristics> characteristics) {
        this(productId, name, description, categoryId, rentalPrice, stock, status, images, characteristics, null);
    }
}
