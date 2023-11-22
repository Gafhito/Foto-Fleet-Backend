package com.digitalhouse.fotofleet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_ratings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id", nullable = false, unique = true)
    private Integer ratingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "review")
    private String review;

    @Column(name = "rating_date", nullable = false)
    private LocalDateTime ratingDate;

    public ProductRating(User user, Product product, Integer rating, String review) {
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.review = review;
        this.ratingDate = LocalDateTime.now();
    }
}
