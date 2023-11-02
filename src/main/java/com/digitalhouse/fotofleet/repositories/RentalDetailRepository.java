package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.RentalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalDetailRepository extends JpaRepository<RentalDetail, Integer> {
}
