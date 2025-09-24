package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.adapter;

import com.hexagonal.ms_foodcourt.domain.model.OrderDish;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderDishResult;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.entity.OrderDishEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.mapper.IOrderDishEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.repository.IOrderDishRepository;
import com.hexagonal.ms_foodcourt.util.TestDataOrderFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderDishJpaAdapterTest {

    @Mock
    private IOrderDishRepository orderDishRepository;

    @Mock
    private IOrderDishEntityMapper orderDishEntityMapper;

    @InjectMocks
    private OrderDishJpaAdapter orderDishJpaAdapter;

    @Test
    void saveAllOrderDishTest() {
        List<OrderDish> orderDishList = TestDataOrderFactory.mockOrderDishList();
        List<OrderDishEntity> entityList = List.of(new OrderDishEntity());

        when(orderDishEntityMapper.toOrderDishEntityList(orderDishList)).thenReturn(entityList);

        orderDishJpaAdapter.saveAllOrderDish(orderDishList);

        verify(orderDishEntityMapper).toOrderDishEntityList(orderDishList);
        verify(orderDishRepository).saveAll(entityList);
    }

    @Test
    void findAllByIdOrderTest() {
        Long orderId = 1L;
        List<OrderDishEntity> entityList = List.of(new OrderDishEntity());
        List<OrderDish> domainList = List.of(new OrderDish());

        when(orderDishRepository.findAllByIdOrder(orderId)).thenReturn(entityList);
        when(orderDishEntityMapper.toOrderDishList(entityList)).thenReturn(domainList);

        List<OrderDish> result = orderDishJpaAdapter.findAllByIdOrder(orderId);

        assertEquals(domainList, result);
        verify(orderDishRepository).findAllByIdOrder(orderId);
        verify(orderDishEntityMapper).toOrderDishList(entityList);
    }

    @Test
    void findAllByIdOrderWithNamesTest() {
        Long orderId = 1L;
        List<OrderDishResult> resultList = List.of(
                new OrderDishResult(1L, "Dish 1", 2),
                new OrderDishResult(2L, "Dish 2", 1)
        );

        when(orderDishRepository.findOrderDishesWithDishNameByIdOrder(orderId)).thenReturn(resultList);

        List<OrderDishResult> result = orderDishJpaAdapter.findAllByIdOrderWithNames(orderId);

        assertEquals(resultList, result);
        verify(orderDishRepository).findOrderDishesWithDishNameByIdOrder(orderId);
    }

}
