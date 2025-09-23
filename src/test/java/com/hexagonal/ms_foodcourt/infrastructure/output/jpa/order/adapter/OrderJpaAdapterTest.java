package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.entity.OrderEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.mapper.IOrderEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.repository.IOrderRepository;
import com.hexagonal.ms_foodcourt.util.TestDataOrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderJpaAdapterTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IOrderEntityMapper orderEntityMapper;

    private OrderJpaAdapter orderJpaAdapter;

    @BeforeEach
    void setUp() {
        orderJpaAdapter = new OrderJpaAdapter(orderRepository, orderEntityMapper);
    }

    @Test
    void existsByIdClientAndStatusListTest() {
        Long clientId = 1L;
        List<String> statuses = List.of("PENDIENTE", "EN_PREPARACION");

        when(orderRepository.existsByIdClientAndStatusIn(clientId, statuses)).thenReturn(true);

        boolean result = orderJpaAdapter.existsByIdClientAndStatusList(clientId, statuses);

        assertTrue(result);
        verify(orderRepository).existsByIdClientAndStatusIn(clientId, statuses);
    }

    @Test
    void existsByIdClientAndStatusListFailTest() {
        Long clientId = 2L;
        List<String> statuses = List.of("LISTO");

        when(orderRepository.existsByIdClientAndStatusIn(clientId, statuses)).thenReturn(false);

        boolean result = orderJpaAdapter.existsByIdClientAndStatusList(clientId, statuses);

        assertFalse(result);
        verify(orderRepository).existsByIdClientAndStatusIn(clientId, statuses);
    }

    @Test
    void saveOrderReturnsIdTest() {
        Order order = TestDataOrderFactory.mockOrder();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(100L);

        when(orderEntityMapper.toEntity(order)).thenReturn(orderEntity);
        when(orderRepository.save(orderEntity)).thenReturn(orderEntity);

        Long savedId = orderJpaAdapter.saveOrder(order);

        assertEquals(100L, savedId);
        verify(orderEntityMapper).toEntity(order);
        verify(orderRepository).save(orderEntity);
    }
}
