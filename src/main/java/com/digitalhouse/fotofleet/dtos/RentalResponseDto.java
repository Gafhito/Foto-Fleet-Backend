package com.digitalhouse.fotofleet.dtos;

import java.time.LocalDateTime;

public record RentalResponseDto(
        Integer rentalDetailId,
        Integer rentalId,
        Integer productId,
        Integer quantity,
        Double rentalPrice,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String status
) {
}
