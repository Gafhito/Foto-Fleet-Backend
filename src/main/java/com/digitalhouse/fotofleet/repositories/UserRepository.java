package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.Rol;
import com.digitalhouse.fotofleet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("select u.roles from User u where u.email = ?1")
    List<Rol> findUserRolesByEmail(String email);
}
