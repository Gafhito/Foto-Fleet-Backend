package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.RentalProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalProductRepository extends JpaRepository<RentalProduct, Integer> {
}
