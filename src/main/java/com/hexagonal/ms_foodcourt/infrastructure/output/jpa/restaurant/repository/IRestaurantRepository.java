package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.repository;

import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    Optional<RestaurantEntity> findByNit(String nit);

    boolean existsByIdAndOwnerId(Long id, Long ownerId);
}
