package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PEDIDOS_PLATOS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(OrderDishId.class)
public class OrderDishEntity {

    @Id
    @Column(name = "id_pedido")
    private Long idOrder;

    @Id
    @Column(name = "id_plato")
    private Long idDish;

    @Column(name = "cantidad", nullable = false)
    private Integer quantity;

}
