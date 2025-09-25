package com.hexagonal.ms_foodcourt.util;

import com.hexagonal.ms_foodcourt.application.dto.request.OrderDishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.domain.model.DeliverOrder;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.OrderDish;
import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.entity.OrderDishEntity;

import java.time.LocalDateTime;
import java.util.List;

public class TestDataOrderFactory {

    public static OrderRequest mockOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setIdRestaurant(1L);

        OrderDishRequest orderDishRequest = new OrderDishRequest();
        orderDishRequest.setIdDish(1L);
        orderDishRequest.setQuantity(2);

        orderRequest.setOrderDishRequests(List.of(orderDishRequest));

        return orderRequest;
    }

    public static OrderReq mockOrderReq() {
        OrderDish orderDish = new OrderDish();
        orderDish.setIdDish(1L);
        orderDish.setQuantity(2);

        OrderReq orderReq = new OrderReq();
        orderReq.setIdRestaurant(1L);
        orderReq.setOrderDishList(List.of(orderDish));

        return orderReq;
    }

    public static List<OrderDish> mockOrderDishList() {
        OrderDish orderDish = new OrderDish();
        orderDish.setIdDish(1L);
        orderDish.setQuantity(2);
        return List.of(orderDish);
    }

    public static List<OrderDishEntity> mockOrderDishEntityList() {
        OrderDishEntity orderDish = new OrderDishEntity();
        orderDish.setIdDish(1L);
        orderDish.setQuantity(2);
        return List.of(orderDish);
    }

    public static Order mockOrder() {
        Order order = new Order();
        order.setId(123L);
        order.setIdClient(1L);
        order.setDate(LocalDateTime.now());
        order.setStatus("PENDIENTE");
        order.setIdRestaurant(1L);
        return order;
    }


    public static DeliverOrder mockDeliverOrder() {
        DeliverOrder order = new DeliverOrder();
        order.setOrderId(1L);
        order.setPin("1234");
        return order;
    }

}
