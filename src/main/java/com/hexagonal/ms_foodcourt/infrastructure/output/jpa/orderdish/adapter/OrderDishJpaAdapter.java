package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.adapter;

import com.hexagonal.ms_foodcourt.domain.model.OrderDish;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderDishPersistencePort;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.entity.OrderDishEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.mapper.IOrderDishEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.repository.IOrderDishRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderDishJpaAdapter implements IOrderDishPersistencePort {

    private final IOrderDishRepository orderDishRepository;

    private final IOrderDishEntityMapper orderDishEntityMapper;


    @Override
    public void saveAllOrderDish(List<OrderDish> orderDishList) {
        List<OrderDishEntity> orderDishEntities = orderDishList.stream()
                .map(orderDishEntityMapper::toEntity)
                .toList();
        orderDishRepository.saveAll(orderDishEntities);
    }
}
