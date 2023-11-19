package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    @Query("select r from Rental r where r.endDate < ?1 and r.status.name = 'Active'")
    List<Rental> findDelayed(LocalDate actualDate);

    @Query("select r from Rental r where r.startDate > ?1 and r.status.name = 'Pending'")
    List<Rental> findPending(LocalDate actualDate);
}
