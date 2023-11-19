package com.digitalhouse.fotofleet.scheduleds;

import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.Rental;
import com.digitalhouse.fotofleet.services.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalScheduled {
    private final RentalService rentalService;

    // Tarea de actualización de status de alquileres vencidos (todos los días a las 00:00hs)
    @Scheduled(cron = "0 0 0 * * ?")
    public void rentalDelayed() throws ResourceNotFoundException {
        List<Rental> rentals = rentalService.listDelayed();

        for (Rental r : rentals) {
            rentalService.changeStatus(r.getRentalId(), "Delayed");
        }
    }

    // Tarea de actualización de status de alquileres pendientes olvidados (todos los días a las 01:00hs)
    @Scheduled(cron = "0 0 1 * * ?")
    public void rentalPending() throws ResourceNotFoundException {
        List<Rental> rentals = rentalService.listPending();

        for (Rental r : rentals) {
            rentalService.changeStatus(r.getRentalId(), "Canceled");
        }
    }
}
