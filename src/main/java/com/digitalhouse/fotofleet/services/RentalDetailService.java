package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.RentalDetail;
import com.digitalhouse.fotofleet.repositories.RentalDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalDetailService {
    private final RentalDetailRepository rentalDetailRepository;

    public RentalDetail getByRentalId(Integer rentalId) throws ResourceNotFoundException {
        Optional<RentalDetail> rentalDetail = rentalDetailRepository.findByRentalId(rentalId);
        if (rentalDetail.isEmpty()) throw new ResourceNotFoundException("No existe el detalle del alquiler con ID " + rentalId);

        return rentalDetail.get();
    }

    public RentalDetail createRentalDetail(RentalDetail rentalDetail) {
        return rentalDetailRepository.save(rentalDetail);
    }

    public List<RentalDetail> listPendingOrActiveByProductIdAndDate(Integer productId, LocalDate startDate, LocalDate endDate) {
        return rentalDetailRepository.findPendingOrActiveByProductIdAndDate(productId, startDate, endDate);
    }
}
