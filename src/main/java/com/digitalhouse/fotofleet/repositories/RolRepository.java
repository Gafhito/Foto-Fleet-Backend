package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    @Query("select r from Rol r where r.roleName = ?1")
    Optional<Rol> findByName(String roleName);
}
