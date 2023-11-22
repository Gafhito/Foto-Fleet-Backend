package com.digitalhouse.fotofleet.dtos;

import java.time.LocalDateTime;

public record ProductRatingDto(
        Integer ratingId,
        UserDto userDto,
        Integer rating,
        String review,
        LocalDateTime ratingDate
) {
    public ProductRatingDto(Integer rating, String review) {
        this(null, null, rating, review, null);
    }
}
