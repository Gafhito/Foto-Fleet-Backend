package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    @Query("select s from Status s where s.name = ?1")
    Optional<Status> findByName(String name);
}
