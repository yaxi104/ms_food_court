package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.Order;

import java.util.List;

public interface IOrderPersistencePort {

    boolean existsByIdClientAndStatusList(Long idClient, List<String> statusList);

    Long saveOrder(Order order);

}
