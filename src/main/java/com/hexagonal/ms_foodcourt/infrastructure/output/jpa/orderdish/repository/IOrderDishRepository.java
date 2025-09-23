package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.repository;

import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.entity.OrderDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderDishRepository extends JpaRepository<OrderDishEntity, Long> {

}
