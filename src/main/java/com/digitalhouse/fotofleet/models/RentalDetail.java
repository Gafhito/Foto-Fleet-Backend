package com.digitalhouse.fotofleet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rental_products")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RentalDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id", nullable = false, unique = true)
    private Integer detailId;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "rental_price_per_day", nullable = false)
    private Double rentalPrice;

    @Column(name = "days_rented", nullable = false)
    private Integer daysRented;

    public RentalDetail(Rental rental, Product product, Integer quantity, Double rentalPrice, Integer daysRented) {
        this.rental = rental;
        this.product = product;
        this.quantity = quantity;
        this.rentalPrice = rentalPrice;
        this.daysRented = daysRented;
    }
}
