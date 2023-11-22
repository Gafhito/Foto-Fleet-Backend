package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRatingRepository extends JpaRepository<ProductRating, Integer> {
    @Query("select pr from ProductRating pr where pr.product.productId = ?1")
    List<ProductRating> findByProductId(Integer productId);
}
