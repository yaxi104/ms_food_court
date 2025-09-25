package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.OrderResponse;
import com.hexagonal.ms_foodcourt.application.handler.IOrderHandler;
import com.hexagonal.ms_foodcourt.application.mapper.IOrderMapper;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderAssignServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderGetListServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderReadyServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderSaveServicePort;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderResult;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderSaveServicePort orderSaveServicePort;
    private final IOrderGetListServicePort orderGetListServicePort;
    private final IOrderReadyServicePort orderReadyServicePort;
    private final IOrderAssignServicePort orderAssignServicePort;
    private final IOrderMapper orderRequestMapper;

    @Override
    public void saveOrder(OrderRequest orderRequest) {
        orderSaveServicePort.saveOrder(orderRequestMapper.toOrderReq(orderRequest));
    }

    @Override
    public PaginatedResponse<OrderResponse> getAllOrderByStatus(String status, Long idRestaurant, Integer page, Integer size) {
        PageResult<OrderResult> resultPage = orderGetListServicePort.getAllOrderByStatus(status, idRestaurant, page, size);

        List<OrderResponse> orderResponses = orderRequestMapper.toOrderResponseList(resultPage.getContent());

        return new PaginatedResponse<>(
                orderResponses,
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.isLast()
        );
    }

    @Override
    public void assignOrderToEmployee(Long orderId) {
        orderAssignServicePort.assignOrderToEmployee(orderId);
    }

    @Override
    public void markOrderAsReady(Long idOrder) {
        orderReadyServicePort.markOrderAsReady(idOrder);
    }
}
