package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.OrderResponse;
import com.hexagonal.ms_foodcourt.application.mapper.IOrderMapper;
import com.hexagonal.ms_foodcourt.domain.api.IOrderServicePort;
import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderResult;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.util.TestDataOrderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderHandlerTest {

    @Mock
    private IOrderServicePort orderServicePort;

    @Mock
    private IOrderMapper orderRequestMapper;

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

    @Test
    void getAllOrderByStatusTest() {
        String status = "PENDING";
        Long restaurantId = 1L;
        int page = 0;
        int size = 2;

        OrderResult order1 = new OrderResult();
        order1.setId(10L);
        order1.setStatus("PENDING");

        OrderResult order2 = new OrderResult();
        order2.setId(11L);
        order2.setStatus("PENDING");

        List<OrderResult> orderResults = List.of(order1, order2);
        PageResult<OrderResult> resultPage = new PageResult<>(orderResults, 3, 6, false);

        when(orderServicePort.getAllOrderByStatus(status, restaurantId, page, size)).thenReturn(resultPage);

        OrderResponse response1 = new OrderResponse();
        response1.setId(10L);
        response1.setStatus("PENDING");

        OrderResponse response2 = new OrderResponse();
        response2.setId(11L);
        response2.setStatus("PENDING");

        List<OrderResponse> responseList = List.of(response1, response2);

        when(orderRequestMapper.toOrderResponseList(orderResults)).thenReturn(responseList);

        PaginatedResponse<OrderResponse> result = orderHandler.getAllOrderByStatus(status, restaurantId, page, size);

        assertNotNull(result);
        assertEquals(responseList, result.getContent());
        assertEquals(3, result.getTotalPages());
        assertEquals(6, result.getTotalElements());
        assertFalse(result.isLast());

        verify(orderServicePort).getAllOrderByStatus(status, restaurantId, page, size);
        verify(orderRequestMapper).toOrderResponseList(orderResults);
    }

    @Test
    void assignOrderToEmployeeCallsService() {
        Long orderId = 123L;

        orderHandler.assignOrderToEmployee(orderId);

        verify(orderServicePort, times(1)).assignOrderToEmployee(orderId);
    }

}
