package com.digitalhouse.fotofleet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false, unique = true)
    private Integer productId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "rental_price_per_day")
    private Double rentalPrice;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    public Product(String name, String description, Category category, Double rentalPrice, Integer stock, Status status) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.rentalPrice = rentalPrice;
        this.stock = stock;
        this.status = status;
    }
}
