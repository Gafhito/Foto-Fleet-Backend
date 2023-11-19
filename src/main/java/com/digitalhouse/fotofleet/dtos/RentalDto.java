package com.digitalhouse.fotofleet.dtos;

import java.sql.Date;
import java.time.LocalDateTime;

public record RentalDto(
        Integer productId,
        Integer quantity,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
