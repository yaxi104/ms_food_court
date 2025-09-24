package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.OrderDish;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderDishResult;

import java.util.List;

public interface IOrderDishPersistencePort {

    void saveAllOrderDish(List<OrderDish> orderDishList);

    List<OrderDish> findAllByIdOrder(Long idOrder);

    List<OrderDishResult> findAllByIdOrderWithNames(Long idOrder);
}
