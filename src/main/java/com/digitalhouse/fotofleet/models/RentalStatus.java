package com.digitalhouse.fotofleet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rental_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RentalStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "status", nullable = false)
    private String status;

    public RentalStatus(String status) {
        this.status = status;
    }
}
