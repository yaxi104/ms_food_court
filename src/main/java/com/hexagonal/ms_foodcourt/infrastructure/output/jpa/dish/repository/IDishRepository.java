package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.repository;

import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {

    Optional<DishEntity> findByNameAndRestaurantId(String name, Long restaurantId);
}
