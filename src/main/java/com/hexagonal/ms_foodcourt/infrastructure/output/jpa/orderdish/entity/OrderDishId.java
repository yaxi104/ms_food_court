package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDishId implements Serializable {

    private Long idOrder;
    private Long idDish;

}