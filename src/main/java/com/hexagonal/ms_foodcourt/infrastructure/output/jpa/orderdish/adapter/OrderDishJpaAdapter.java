package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.adapter;

import com.hexagonal.ms_foodcourt.domain.model.OrderDish;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderDishResult;
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
        List<OrderDishEntity> orderDishEntities = orderDishEntityMapper.toOrderDishEntityList(orderDishList);
        orderDishRepository.saveAll(orderDishEntities);
    }

    @Override
    public List<OrderDish> findAllByIdOrder(Long idOrder) {
        List<OrderDishEntity> orderDishEntities = orderDishRepository.findAllByIdOrder(idOrder);
        return orderDishEntityMapper.toOrderDishList(orderDishEntities);
    }

    @Override
    public List<OrderDishResult> findAllByIdOrderWithNames(Long idOrder) {
        return orderDishRepository.findOrderDishesWithDishNameByIdOrder(idOrder);
    }

}
