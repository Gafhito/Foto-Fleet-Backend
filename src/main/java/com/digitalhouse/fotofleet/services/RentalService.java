package com.digitalhouse.fotofleet.services;

import com.digitalhouse.fotofleet.dtos.RentalDto;
import com.digitalhouse.fotofleet.dtos.RentalResponseDto;
import com.digitalhouse.fotofleet.exceptions.BadRequestException;
import com.digitalhouse.fotofleet.exceptions.ResourceNotFoundException;
import com.digitalhouse.fotofleet.models.*;
import com.digitalhouse.fotofleet.repositories.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;
    private final UserService userService;
    private final ProductService productService;
    private final StatusService statusService;
    private final RentalDetailService rentalDetailService;

    public Rental getById(Integer id) throws ResourceNotFoundException {
        Optional<Rental> rental = rentalRepository.findById(id);
        if (rental.isEmpty()) throw new ResourceNotFoundException("No existe el alquiler con ID: " + id);

        return rental.get();
    }

    public List<Rental> listDelayed() {
        LocalDate actualDate = LocalDate.now();
        return rentalRepository.findDelayed(actualDate);
    }

    public List<Rental> listPending() {
        LocalDate actualDate = LocalDate.now();
        return rentalRepository.findPending(actualDate.plusDays(1));
    }

    public Rental createRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public void deleteRental(Integer id) throws ResourceNotFoundException {
        Optional<Rental> rental = rentalRepository.findById(id);
        if(rental.isEmpty()) throw new ResourceNotFoundException("No existe alquiler con ID: " + id);

        rentalRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<RentalResponseDto> addRentals(String jwt, List<RentalDto> rentalDtos) throws BadRequestException, ResourceNotFoundException {
        User user = userService.getUserByJwt(jwt);
        if (rentalDtos.size() > 20) throw new BadRequestException("El carrito de compra no puede exceder los 20 artículos");

        List<RentalResponseDto> rentalResponseDtos = new ArrayList<>();
        for (RentalDto rentalDto : rentalDtos) {
            Optional<Product> product = productService.getById(rentalDto.productId());
            if (product.isEmpty()) throw new ResourceNotFoundException("No existe el producto con ID " + rentalDto.productId() + " enviado en el listado");

            Optional<Status> status = statusService.getStatusByName("Pending");
            if (status.isEmpty()) throw new ResourceNotFoundException("No existe el estatus Pending, póngase en contacto con soporte");
            if (product.get().getStock() < rentalDto.quantity()) throw new BadRequestException("Excede el stock máximo de productos con ID " + product.get().getProductId());
            if (rentalDto.startDate().isAfter(rentalDto.endDate())) throw new BadRequestException("La fecha de inicio del alquiler no puede exceder la fecha de fin del mismo");
            if (Duration.between(rentalDto.startDate(), rentalDto.endDate()).toDays() > 90) throw new BadRequestException("El alquiler del producto no puede exceder los 3 meses");

            List<RentalDetail> rentalDetails = rentalDetailService.listPendingOrActiveByProductIdAndDate(product.get().getProductId(), rentalDto.startDate().toLocalDate(), rentalDto.endDate().toLocalDate());
            if (rentalDetails.size() >= rentalDto.quantity()) throw new BadRequestException("Lo sentimos, en el rango de fechas indicadas no habrá disponible el stock suficiente de " + product.get().getName() + " para que pueda alquilar");

            Integer daysRented = (int) Duration.between(rentalDto.startDate(), rentalDto.endDate()).toDays();
            Double rentalPrice = (product.get().getRentalPrice() * rentalDto.quantity()) * daysRented;

            Rental rental = createRental(new Rental(user, rentalDto.startDate().toLocalDate(), rentalDto.endDate().toLocalDate(), status.get()));
            RentalDetail rentalDetail = rentalDetailService.createRentalDetail(new RentalDetail(rental, product.get(), rentalDto.quantity(), rentalPrice, daysRented));

            rentalResponseDtos.add(new RentalResponseDto(rentalDetail.getDetailId(), rental.getRentalId(), product.get().getProductId(), rentalDetail.getQuantity(), rentalDetail.getRentalPrice(), rental.getStartDate(), rental.getEndDate(), status.get().getName()));
        }

        return rentalResponseDtos;
    }

    public RentalResponseDto changeStatus(Integer rentalId, String status) throws ResourceNotFoundException {
        Optional<Status> s = statusService.getStatusByName(status);
        if (s.isEmpty()) throw new ResourceNotFoundException("No existe el status " + status);

        RentalDetail rentalDetail = rentalDetailService.getByRentalId(rentalId);
        Rental rental = getById(rentalId);
        rental.setStatus(s.get());
        Rental newRental = rentalRepository.save(rental);

        return new RentalResponseDto(rentalDetail.getDetailId(), newRental.getRentalId(), rentalDetail.getProduct().getProductId(), rentalDetail.getQuantity(), rentalDetail.getRentalPrice(), newRental.getStartDate(), newRental.getEndDate(), newRental.getStatus().getName());
    }
}
