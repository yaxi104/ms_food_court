package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderResult;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.entity.OrderEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.mapper.IOrderEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.repository.IOrderRepository;
import com.hexagonal.ms_foodcourt.util.TestDataOrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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

    @Test
    void findByStatusAndIdRestaurantTest() {
        String status = "PENDING";
        Long restaurantId = 1L;
        PageInfo pageInfo = new PageInfo(0, 2, "date");

        List<OrderEntity> orderEntities = List.of(new OrderEntity(), new OrderEntity());
        List<OrderResult> orderResults = List.of(new OrderResult(), new OrderResult());

        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), Sort.by(pageInfo.getSortBy()));
        Page<OrderEntity> mockPage = new PageImpl<>(orderEntities, pageable, 2);

        when(orderRepository.findByStatusAndIdRestaurant(status, restaurantId, pageable)).thenReturn(mockPage);
        when(orderEntityMapper.toOrderList(orderEntities)).thenReturn(orderResults);

        PageResult<OrderResult> result = orderJpaAdapter.findByStatusAndIdRestaurant(status, restaurantId, pageInfo);

        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());

        verify(orderRepository).findByStatusAndIdRestaurant(status, restaurantId, pageable);
        verify(orderEntityMapper).toOrderList(orderEntities);
    }

    @Test
    void findByIdReturnsOrderWhenFound() {
        Long orderId = 1L;
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(orderId);
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderEntityMapper.toOrder(orderEntity)).thenReturn(order);

        Optional<Order> result = orderJpaAdapter.findById(orderId);

        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().getId());

        verify(orderRepository).findById(orderId);
        verify(orderEntityMapper).toOrder(orderEntity);
    }

    @Test
    void findByIdReturnsEmptyWhenNotFound() {
        Long orderId = 2L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Optional<Order> result = orderJpaAdapter.findById(orderId);

        assertFalse(result.isPresent());

        verify(orderRepository).findById(orderId);
        verifyNoInteractions(orderEntityMapper);
    }

}
