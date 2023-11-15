package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select p from Product p")
    Page<Product> listAllProducts(Pageable page);

    @Query("select p from Product p where p.name = ?1")
    Optional<Product> findByName(String name);

    @Query("select p from Product p " +
            "where p.name like %?1% " +
            "or p.description like %?1% " +
            "or p.category.name like %?2%")
    List<Product> findByFilter(String product, String categoryName);
}
