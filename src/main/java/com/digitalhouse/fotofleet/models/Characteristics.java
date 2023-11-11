package com.digitalhouse.fotofleet.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_attributes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Characteristics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_id", nullable = false, unique = true)
    private Integer characteristicsId;

    @Column(name = "attribute_name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "font_awesome_class")
    private String urlIcono;

    public Characteristics(String name, String description, String urlIcono) {
        this.name = name;
        this.description = description;
        this.urlIcono = urlIcono;
    }
}
