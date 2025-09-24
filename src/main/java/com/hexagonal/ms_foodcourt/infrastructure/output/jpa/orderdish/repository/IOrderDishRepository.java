package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.repository;

import com.hexagonal.ms_foodcourt.domain.model.response.OrderDishResult;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.entity.OrderDishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IOrderDishRepository extends JpaRepository<OrderDishEntity, Long> {

    List<OrderDishEntity> findAllByIdOrder(Long idOrder);


    @Query("""
            SELECT new com.hexagonal.ms_foodcourt.domain.model.response.OrderDishResult(od.idDish, d.name, od.quantity)
            FROM OrderDishEntity od
            JOIN DishEntity d ON od.idDish = d.id
            WHERE od.idOrder = :idOrder
            """)
    List<OrderDishResult> findOrderDishesWithDishNameByIdOrder(@Param("idOrder") Long idOrder);

}
