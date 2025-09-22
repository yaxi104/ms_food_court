package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PLATOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "precio", nullable = false)
    private Integer price;

    @Column(name = "descripcion", nullable = false)
    private String description;

    @Column(name = "url_imagen", nullable = false)
    private String imageUrl;

    @Column(name = "id_categoria", nullable = false)
    private Long categoryId;

    @Column(name = "id_restaurante", nullable = false)
    private Long restaurantId;

    @Column(name = "activo", nullable = false, length = 5)
    private String active;

}
