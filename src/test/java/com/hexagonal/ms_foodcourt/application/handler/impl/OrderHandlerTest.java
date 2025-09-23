package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.application.mapper.IOrderRequestMapper;
import com.hexagonal.ms_foodcourt.domain.api.IOrderServicePort;
import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import com.hexagonal.ms_foodcourt.util.TestDataOrderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderHandlerTest {

    @Mock
    private IOrderServicePort orderServicePort;

    @Mock
    private IOrderRequestMapper orderRequestMapper;

    @InjectMocks
    private OrderHandler orderHandler;

    @Test
    void saveOrderSuccessTest() {
        OrderRequest orderRequest = TestDataOrderFactory.mockOrderRequest();

        OrderReq mockOrderReq = new OrderReq();

        when(orderRequestMapper.toOrderReq(orderRequest)).thenReturn(mockOrderReq);

        orderHandler.saveOrder(orderRequest);

        verify(orderRequestMapper).toOrderReq(orderRequest);
        verify(orderServicePort).saveOrder(mockOrderReq);
    }
}