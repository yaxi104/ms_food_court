package com.hexagonal.ms_foodcourt.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hexagonal.ms_foodcourt.application.dto.request.DeliverOrderRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.OrderResponse;
import com.hexagonal.ms_foodcourt.application.handler.IOrderHandler;
import com.hexagonal.ms_foodcourt.infrastructure.exceptionhandler.ControllerAdvisor;
import com.hexagonal.ms_foodcourt.util.TestDataOrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderRestControllerTest {

    private MockMvc mockMvc;
    private IOrderHandler orderHandler;

    private JacksonTester<OrderRequest> orderRequestTester;

    @BeforeEach
    void setUp() {
        orderHandler = mock(IOrderHandler.class);
        OrderRestController controller = new OrderRestController(orderHandler);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        JacksonTester.initFields(this, objectMapper);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerAdvisor())
                .build();

        var auth = new TestingAuthenticationToken("cliente", "password", "ROLE_CLIENTE");
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void saveOrderSuccess() throws Exception {
        OrderRequest request = TestDataOrderFactory.mockOrderRequest();

        mockMvc.perform(post("/api/v1/order/customer")
                        .with(authentication(SecurityContextHolder.getContext().getAuthentication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestTester.write(request).getJson()))
                .andExpect(status().isCreated());

        verify(orderHandler).saveOrder(any(OrderRequest.class));
    }

    @Test
    void getAllOrderByStatusSuccess() throws Exception {
        String status = "PENDIENTE";
        Long idRestaurant = 1L;
        int page = 0;
        int size = 2;

        OrderResponse order1 = new OrderResponse();
        order1.setId(101L);
        order1.setStatus(status);
        order1.setOrderDishResponses(Collections.emptyList());

        OrderResponse order2 = new OrderResponse();
        order2.setId(102L);
        order2.setStatus(status);
        order2.setOrderDishResponses(Collections.emptyList());

        List<OrderResponse> orderResponses = List.of(order1, order2);


        PaginatedResponse<OrderResponse> response = new PaginatedResponse<>(
                orderResponses, 1, 2, true
        );

        when(orderHandler.getAllOrderByStatus(status, idRestaurant, page, size)).thenReturn(response);

        mockMvc.perform(get("/api/v1/order/all")
                        .param("status", status)
                        .param("idRestaurant", idRestaurant.toString())
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .with(authentication(new TestingAuthenticationToken("empleado", "password", "ROLE_EMPLEADO")))
                )
                .andExpect(status().isOk());

        verify(orderHandler).getAllOrderByStatus(status, idRestaurant, page, size);
    }

    @Test
    void assignOrderToEmployeeSuccess() throws Exception {
        Long orderId = 123L;

        mockMvc.perform(post("/api/v1/order/assign/{orderId}", orderId)
                        .with(authentication(new TestingAuthenticationToken("empleado", "password", "ROLE_EMPLEADO"))))
                .andExpect(status().isOk());

        verify(orderHandler).assignOrderToEmployee(orderId);
    }

    @Test
    void markOrderAsReadySuccessfulTest() throws Exception {
        Long orderId = 1L;

        mockMvc.perform(post("/api/v1/order/ready/{orderId}", orderId))
                .andExpect(status().isOk());

        verify(orderHandler).markOrderAsReady(orderId);
    }

    @Test
    void markOrderAsDeliveredSuccess() throws Exception {
        DeliverOrderRequest request = new DeliverOrderRequest();
        request.setOrderId(1L);
        request.setPin("ABC123");

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/order/delivered")
                        .with(authentication(new TestingAuthenticationToken("empleado", "password", "ROLE_EMPLEADO")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(orderHandler).markOrderAsDelivered(any(DeliverOrderRequest.class));
    }
}