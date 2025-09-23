package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.repository;

import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByIdClientAndStatusIn(Long clienteId, List<String> estados);

}
