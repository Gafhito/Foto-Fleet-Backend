package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.Characteristics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacteristicsRepository extends JpaRepository<Characteristics, Integer> {
}
