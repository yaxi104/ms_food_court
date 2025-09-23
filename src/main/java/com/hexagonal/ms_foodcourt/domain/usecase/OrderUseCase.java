package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.api.IOrderServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.DishNotRestaurantException;
import com.hexagonal.ms_foodcourt.domain.exception.OrdenByIdClientExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.OrderDish;
import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import com.hexagonal.ms_foodcourt.domain.spi.IDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;

import java.time.LocalDateTime;
import java.util.List;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.LISTO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;

public class OrderUseCase implements IOrderServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;

    public OrderUseCase(IDishPersistencePort dishPersistencePort,
                        IOrderPersistencePort orderPersistencePort,
                        IOrderDishPersistencePort orderDishPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
    }


    @Override
    public void saveOrder(OrderReq orderReq) {
        if (orderPersistencePort.existsByIdClientAndStatusList(orderReq.getIdClient(), List.of(EN_PREPARACION, PENDIENTE, LISTO))) {
            throw new OrdenByIdClientExistsException();
        }

        List<OrderDish> orderDishList = orderReq.getOrderDishList();

        List<Long> dishIds = orderDishList.stream()
                .map(OrderDish::getIdDish)
                .toList();

        Long countValidDishes = dishPersistencePort.countValidDishesByRestaurant(dishIds, orderReq.getIdRestaurant());

        if (countValidDishes != dishIds.size()) {
            throw new DishNotRestaurantException();
        }

        Order order = new Order();
        order.setIdClient(orderReq.getIdClient());
        order.setDate(LocalDateTime.now());
        order.setStatus(PENDIENTE);
        order.setIdRestaurant(orderReq.getIdRestaurant());

        Long orderId = orderPersistencePort.saveOrder(order);
        orderDishList.forEach(orderDish -> orderDish.setIdOrder(orderId));

        orderDishPersistencePort.saveAllOrderDish(orderDishList);
    }
}
