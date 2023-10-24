package com.digitalhouse.fotofleet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "rental_status", nullable = false)
    private RentalStatus rentalStatus;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public Rental(User user, RentalStatus rentalStatus, LocalDate startDate, LocalDate endDate) {
        this.user = user;
        this.rentalStatus = rentalStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
