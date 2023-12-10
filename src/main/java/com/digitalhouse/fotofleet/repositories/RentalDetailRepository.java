package com.digitalhouse.fotofleet.repositories;

import com.digitalhouse.fotofleet.models.RentalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalDetailRepository extends JpaRepository<RentalDetail, Integer> {
    @Query("select rd from RentalDetail rd where rd.rental.rentalId = ?1")
    Optional<RentalDetail> findByRentalId(Integer rentalId);

    @Query("select rd from RentalDetail rd " +
            "where rd.product.productId = ?1 " +
            "and (rd.rental.startDate >= ?2 and rd.rental.endDate <= ?3) " +
            "and (rd.rental.status.name = 'Pending' or rd.rental.status.name = 'Active')")
    List<RentalDetail> findPendingOrActiveByProductIdAndDate(Integer productId, LocalDate startDate, LocalDate endDate);

    @Query("select rd from RentalDetail rd where rd.rental.user.userId = ?1")
    List<RentalDetail> findByUserId(Integer userId);

    @Query("select rd from RentalDetail rd where rd.rental.user.userId = ?1 and rd.rental.status.name = ?2")
    List<RentalDetail> findByUserIdAndStatus(Integer userId, String status);

    @Query("select rd from RentalDetail rd where rd.product.productId = ?1 and (rd.rental.status.name = 'Pending' or rd.rental.status.name = 'Active')")
    List<RentalDetail> findPendingAndActiveByProductId(Integer productId);

    @Query("select rd from RentalDetail rd where rd.rental.user.userId = ?1 and rd.product.productId = ?2 and rd.rental.status.name = 'Completed'")
    List<RentalDetail> checkUserRentalProduct(Integer userId, Integer productId);
}
