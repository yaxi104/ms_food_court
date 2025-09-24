package com.hexagonal.ms_foodcourt.domain.api;

import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderResult;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;

public interface IOrderServicePort {

    void saveOrder(OrderReq orderReq);

    PageResult<OrderResult> getAllOrderByStatus(String status, Long idRestaurant, Integer page, Integer size);

    void assignOrderToEmployee(Long orderId);
}
