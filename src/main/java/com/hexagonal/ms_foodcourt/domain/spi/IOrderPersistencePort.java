package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderResult;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;

import java.util.List;
import java.util.Optional;

public interface IOrderPersistencePort {

    boolean existsByIdClientAndStatusList(Long idClient, List<String> statusList);

    Long saveOrder(Order order);

    PageResult<OrderResult> findByStatusAndIdRestaurant(String status, Long restaurantId, PageInfo pageInfo);

    Optional<Order> findById(Long id);
}
