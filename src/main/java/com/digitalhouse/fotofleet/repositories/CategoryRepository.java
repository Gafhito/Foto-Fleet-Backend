package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("select c from Category c where c.name = ?1")
    Optional<Category> findByName(String name);
}
