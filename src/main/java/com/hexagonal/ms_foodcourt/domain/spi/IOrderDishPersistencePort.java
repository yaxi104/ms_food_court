package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.OrderDish;

import java.util.List;

public interface IOrderDishPersistencePort {

    void saveAllOrderDish(List<OrderDish> orderDishList);

}
