package com.hexagonal.ms_foodcourt.domain.usecase.spi;

import com.hexagonal.ms_foodcourt.domain.model.Dish;

import java.util.Optional;

public interface IDishPersistencePort {

    void saveDish(Dish dish);

    Optional<Dish> findById(Long idDish);

    Optional<Dish> findByNameAndRestaurantId(String name, Long idRestaurante);
}
