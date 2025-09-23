package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.entity.OrderEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.mapper.IOrderEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;

    private final IOrderEntityMapper orderEntityMapper;


    @Override
    public boolean existsByIdClientAndStatusList(Long idClient, List<String> statusList) {
        return orderRepository.existsByIdClientAndStatusIn(idClient, statusList);
    }

    @Override
    public Long saveOrder(Order order) {
        OrderEntity orderEntity = orderRepository.save(orderEntityMapper.toEntity(order));
        return orderEntity.getId();
    }
}
