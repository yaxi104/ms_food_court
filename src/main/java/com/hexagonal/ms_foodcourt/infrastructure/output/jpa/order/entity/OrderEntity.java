package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.entity;

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

import java.time.LocalDateTime;

@Entity
@Table(name = "PEDIDOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cliente", nullable = false)
    private Long idClient;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime date;

    @Column(name = "estado", nullable = false, length = 20)
    private String status;

    @Column(name = "id_chef")
    private Long idChef;

    @Column(name = "id_restaurante", nullable = false)
    private Long idRestaurant;

}
