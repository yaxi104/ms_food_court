package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.application.handler.IOrderHandler;
import com.hexagonal.ms_foodcourt.application.mapper.IOrderRequestMapper;
import com.hexagonal.ms_foodcourt.domain.api.IOrderServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;

    @Override
    public void saveOrder(OrderRequest orderRequest) {
        orderServicePort.saveOrder(orderRequestMapper.toOrderReq(orderRequest));
    }
}
