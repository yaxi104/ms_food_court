package com.hexagonal.ms_foodcourt.application.handler;


import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.OrderResponse;

public interface IOrderHandler {

    void saveOrder(OrderRequest orderRequest);

    PaginatedResponse<OrderResponse> getAllOrderByStatus(String status, Long idRestaurant, Integer page, Integer size);

    void assignOrderToEmployee(Long orderId);
}
