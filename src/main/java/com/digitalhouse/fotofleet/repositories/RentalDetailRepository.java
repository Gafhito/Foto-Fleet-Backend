package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.RentalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalDetailRepository extends JpaRepository<RentalDetail, Integer> {
    @Query("select rd from RentalDetail rd where rd.rental.rentalId = ?1")
    Optional<RentalDetail> findByRentalId(Integer rentalId);
}
