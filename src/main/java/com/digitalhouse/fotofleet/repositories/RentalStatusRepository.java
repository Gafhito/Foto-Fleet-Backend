package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalStatusRepository extends JpaRepository<RentalStatus, Integer> {
}
