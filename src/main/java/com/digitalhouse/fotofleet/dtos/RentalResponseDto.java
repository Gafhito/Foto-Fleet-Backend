package com.digitalhouse.fotofleet.dtos;

import java.time.LocalDate;

public record RentalResponseDto(
        Integer rentalDetailId,
        Integer rentalId,
        Integer productId,
        Integer quantity,
        Double rentalPrice,
        LocalDate startDate,
        LocalDate endDate,
        String status
) {
}
