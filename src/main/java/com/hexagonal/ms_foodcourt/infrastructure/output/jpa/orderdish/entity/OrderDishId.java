package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
public class OrderDishId implements Serializable {

    private Long idOrder;
    private Long idDish;

    public OrderDishId() {
    }

    public OrderDishId(Long idOrder, Long idDish) {
        this.idOrder = idOrder;
        this.idDish = idDish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDishId that)) return false;
        return Objects.equals(idOrder, that.idOrder) &&
                Objects.equals(idDish, that.idDish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrder, idDish);
    }

}