package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.adapter;

import com.hexagonal.ms_foodcourt.domain.model.OrderDish;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.entity.OrderDishEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.mapper.IOrderDishEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.repository.IOrderDishRepository;
import com.hexagonal.ms_foodcourt.util.TestDataOrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderDishJpaAdapterTest {

    @Mock
    private IOrderDishRepository orderDishRepository;

    @Mock
    private IOrderDishEntityMapper orderDishEntityMapper;

    private OrderDishJpaAdapter orderDishJpaAdapter;

    @BeforeEach
    void setUp() {
        orderDishJpaAdapter = new OrderDishJpaAdapter(orderDishRepository, orderDishEntityMapper);
    }

    @Test
    void saveAllOrderDishTest() {
        List<OrderDish> orderDishList = TestDataOrderFactory.mockOrderDishList();

        OrderDishEntity entity1 = new OrderDishEntity();

        when(orderDishEntityMapper.toEntity(orderDishList.get(0))).thenReturn(entity1);

        orderDishJpaAdapter.saveAllOrderDish(orderDishList);

        verify(orderDishEntityMapper).toEntity(orderDishList.get(0));
        verify(orderDishRepository).saveAll(List.of(entity1));
    }

    @Test
    void saveAllOrderDish_WithEmptyList_DoesNothing() {
        List<OrderDish> emptyList = List.of();

        orderDishJpaAdapter.saveAllOrderDish(emptyList);

        verify(orderDishRepository).saveAll(List.of());
        verifyNoInteractions(orderDishEntityMapper);
    }
}
