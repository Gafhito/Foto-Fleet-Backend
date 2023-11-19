package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.RentalDetail;
import com.digitalhouse.fotofleet.repositories.RentalDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalDetailService {
    private final RentalDetailRepository rentalDetailRepository;

    public RentalDetail getById(Integer id) throws ResourceNotFoundException {
        Optional<RentalDetail> rentalDetail = rentalDetailRepository.findById(id);
        if (rentalDetail.isEmpty()) throw new ResourceNotFoundException("No existe el detalle del alquiler con ID " + id);

        return rentalDetail.get();
    }

    public RentalDetail getByRentalId(Integer rentalId) throws ResourceNotFoundException {
        Optional<RentalDetail> rentalDetail = rentalDetailRepository.findByRentalId(rentalId);
        if (rentalDetail.isEmpty()) throw new ResourceNotFoundException("No existe el detalle del alquiler con ID " + rentalId);

        return rentalDetail.get();
    }

    public RentalDetail createRentalDetail(RentalDetail rentalDetail) {
        return rentalDetailRepository.save(rentalDetail);
    }
}
