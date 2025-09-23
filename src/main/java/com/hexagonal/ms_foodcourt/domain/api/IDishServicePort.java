package com.hexagonal.ms_foodcourt.domain.api;

import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;

public interface IDishServicePort {

    void saveDish(Dish dish);

    void updateDish(Dish dish);

    void toggleStatusDish(Dish dish);

    PageResult<Dish> getListDish(Long restaurantId, Long categoryId, Integer page, Integer size);
}
