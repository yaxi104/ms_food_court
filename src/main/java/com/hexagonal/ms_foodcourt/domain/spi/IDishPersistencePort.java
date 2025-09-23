package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.PageResult;

import java.util.List;
import java.util.Optional;

public interface IDishPersistencePort {

    void saveDish(Dish dish);

    Optional<Dish> findById(Long idDish);

    Optional<Dish> findByNameAndRestaurantId(String name, Long idRestaurante);

    PageResult<Dish> listDishes(Long restaurantId, Long categoryId, PageInfo pageInfo);

    Long countValidDishesByRestaurant(List<Long> dishIds, Long restaurantId);
}
