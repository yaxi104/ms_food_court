package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IDishPersistencePort {

    void saveDish(Dish dish);

    Optional<Dish> findById(Long idDish);

    Optional<Dish> findByNameAndRestaurantId(String name, Long idRestaurante);

    Page<Dish> listDishes(Long restaurantId, Long categoryId, Pageable pageable);

    Long countValidDishesByRestaurant(List<Long> dishIds, Long restaurantId);
}
