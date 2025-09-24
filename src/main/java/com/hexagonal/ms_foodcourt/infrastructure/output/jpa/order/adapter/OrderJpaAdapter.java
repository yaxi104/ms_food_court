package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderResult;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.entity.OrderEntity;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.mapper.IOrderEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

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

    @Override
    public PageResult<OrderResult> findByStatusAndIdRestaurant(String status, Long restaurantId, PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.getPage(), pageInfo.getSize(), Sort.by(pageInfo.getSortBy()));

        Page<OrderEntity> page = orderRepository.findByStatusAndIdRestaurant(status, restaurantId, pageable);
        List<OrderResult> content = orderEntityMapper.toOrderList(page.getContent());
        return new PageResult<>(content, page.getTotalPages(), page.getTotalElements(), page.isLast());
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id)
                .map(orderEntityMapper::toOrder);
    }

}
