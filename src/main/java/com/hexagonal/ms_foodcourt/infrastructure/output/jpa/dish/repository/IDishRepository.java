package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.repository;

import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.entity.DishEntity;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {

    Optional<DishEntity> findByNameAndRestaurantId(String name, Long restaurantId);

    Page<DishEntity> findByRestaurantIdAndCategoryId(Long restaurantId, Long categoryId, Pageable pageable);

    Page<DishEntity> findByRestaurantId(Long restaurantId, Pageable pageable);

    @Query("SELECT COUNT(d) FROM DishEntity d WHERE d.id IN :dishIds AND d.restaurantId = :restaurantId")
    long countValidDishesByRestaurant(@Param("dishIds") List<Long> dishIds, @Param("restaurantId") Long restaurantId);

}
