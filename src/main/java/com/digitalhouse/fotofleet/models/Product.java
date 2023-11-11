package com.digitalhouse.fotofleet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_attribute_associations",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_id", referencedColumnName = "attribute_id")
    )
    private List<Characteristics> characteristics;

    /*public Product(String name, String description, Category category, Double rentalPrice, Integer stock, Status status, List<Characteristics> characteristics) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.rentalPrice = rentalPrice;
        this.stock = stock;
        this.status = status;
        this.characteristics = characteristics;
    }*/

    public Product(String name, String description, Category category, Double rentalPrice, Integer stock, Status status) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.rentalPrice = rentalPrice;
        this.stock = stock;
        this.status = status;
    }
}
