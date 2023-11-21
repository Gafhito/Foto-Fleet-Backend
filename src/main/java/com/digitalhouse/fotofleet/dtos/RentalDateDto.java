package com.digitalhouse.fotofleet.dtos;

import java.time.LocalDate;

public record RentalDateDto(
        Integer rentalId,
        LocalDate startDate,
        LocalDate endDate
) {
}
