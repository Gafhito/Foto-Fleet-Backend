package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    @Query("select pi from ProductImage pi where pi.product.productId = ?1 order by pi.primary desc")
    List<ProductImage> listByProductId(Integer productId);

    @Query("select pi from ProductImage pi where pi.product.productId = ?1 and pi.primary = true")
    Optional<ProductImage> findPrimaryImageByProductId(Integer productId);
}
