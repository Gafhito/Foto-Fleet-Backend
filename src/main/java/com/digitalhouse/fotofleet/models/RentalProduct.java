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
public class RentalProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price_per_day", nullable = false)
    private Double pricePerDay;

    public RentalProduct(Rental rental, Product product, Integer quantity, Double pricePerDay) {
        this.rental = rental;
        this.product = product;
        this.quantity = quantity;
        this.pricePerDay = pricePerDay;
    }
}
