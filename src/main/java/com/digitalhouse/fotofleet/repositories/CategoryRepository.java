package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
