package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.RentalResponseDto;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.RentalDetail;
import com.digitalhouse.fotofleet.repositories.RentalDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalDetailService {
    private final RentalDetailRepository rentalDetailRepository;

    public RentalDetail getByRentalId(Integer rentalId) throws ResourceNotFoundException {
        Optional<RentalDetail> rentalDetail = rentalDetailRepository.findByRentalId(rentalId);
        if (rentalDetail.isEmpty()) throw new ResourceNotFoundException("No existe el detalle del alquiler con ID: " + rentalId);

        return rentalDetail.get();
    }

    public RentalDetail createRentalDetail(RentalDetail rentalDetail) {
        return rentalDetailRepository.save(rentalDetail);
    }


    public List<RentalDetail> listPendingOrActiveByProductIdAndDate(Integer productId, LocalDate startDate, LocalDate endDate) {
        return rentalDetailRepository.findPendingOrActiveByProductIdAndDate(productId, startDate, endDate);
    }

    public List<RentalDetail> listPendingAndActiveByProductId(Integer productId) {
        return rentalDetailRepository.findPendingAndActiveByProductId(productId);
    }

    public List<RentalResponseDto> listByUserId(Integer userId) {
        List<RentalDetail> rentalDetails = rentalDetailRepository.findByUserId(userId);
        List<RentalResponseDto> rentalResponseDtos = new ArrayList<>();

        for (RentalDetail rd : rentalDetails) {
            rentalResponseDtos.add(new RentalResponseDto(rd.getDetailId(), rd.getRental().getRentalId(), rd.getProduct().getProductId(), rd.getQuantity(), rd.getRentalPrice(), rd.getRental().getStartDate(), rd.getRental().getEndDate(), rd.getRental().getStatus().getName()));
        }

        return rentalResponseDtos;
    }

    public Boolean checkUserRentalProduct(Integer userId, Integer productId) {
        List<RentalDetail> rentalDetails = rentalDetailRepository.checkUserRentalProduct(userId, productId);

        return rentalDetails.isEmpty();
    }
}
