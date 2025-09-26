package com.hexagonal.ms_foodcourt.application.handler.impl;

import com.hexagonal.ms_foodcourt.application.dto.request.DeliverOrderRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.MessageResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.OrderResponse;
import com.hexagonal.ms_foodcourt.application.mapper.IOrderMapper;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderAssignServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderCanceledServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderDeliveredServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderGetListServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderReadyServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderSaveServicePort;
import com.hexagonal.ms_foodcourt.domain.model.DeliverOrder;
import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import com.hexagonal.ms_foodcourt.domain.model.response.MessageResult;
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
    private IOrderSaveServicePort orderSaveServicePort;

    @Mock
    private IOrderGetListServicePort orderGetListServicePort;

    @Mock
    private IOrderAssignServicePort orderAssignServicePort;

    @Mock
    private IOrderReadyServicePort orderReadyServicePort;

    @Mock
    private IOrderDeliveredServicePort orderDeliveredServicePort;

    @Mock
    private IOrderCanceledServicePort orderCanceledServicePort;

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
        verify(orderSaveServicePort).saveOrder(mockOrderReq);
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

        when(orderGetListServicePort.getAllOrderByStatus(status, restaurantId, page, size)).thenReturn(resultPage);

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

        verify(orderGetListServicePort).getAllOrderByStatus(status, restaurantId, page, size);
        verify(orderRequestMapper).toOrderResponseList(orderResults);
    }

    @Test
    void assignOrderToEmployeeCallsService() {
        Long orderId = 123L;

        orderHandler.assignOrderToEmployee(orderId);

        verify(orderAssignServicePort, times(1)).assignOrderToEmployee(orderId);
    }

    @Test
    void markOrderAsReadyTest() {
        Long orderId = 123L;

        orderHandler.markOrderAsReady(orderId);

        verify(orderReadyServicePort, times(1)).markOrderAsReady(orderId);
    }

    @Test
    void markOrderAsDeliveredTest() {
        DeliverOrderRequest request = new DeliverOrderRequest();
        request.setOrderId(1L);
        request.setPin("123456");

        DeliverOrder deliverOrder = new DeliverOrder(1L, "123456");

        when(orderRequestMapper.toDeliverOrder(request)).thenReturn(deliverOrder);
        orderHandler.markOrderAsDelivered(request);
        verify(orderDeliveredServicePort, times(1)).markOrderAsDelivered(deliverOrder);
    }


    @Test
    void shouldReturnMessageResponseWhenOrderIsCanceled() {
        Long orderId = 1L;

        MessageResult messageResult = new MessageResult("Your order has been canceled");
        MessageResponse expectedResponse = new MessageResponse("Your order has been canceled");

        when(orderCanceledServicePort.markOrderAsCanceled(orderId)).thenReturn(messageResult);
        when(orderRequestMapper.toMessageResult(messageResult)).thenReturn(expectedResponse);

        MessageResponse actualResponse = orderHandler.markOrderAsCanceled(orderId);

        assertEquals("Your order has been canceled", actualResponse.getMessage());
        verify(orderCanceledServicePort).markOrderAsCanceled(orderId);
        verify(orderRequestMapper).toMessageResult(messageResult);
    }
}
