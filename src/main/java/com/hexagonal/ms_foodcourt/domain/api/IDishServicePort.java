package com.hexagonal.ms_foodcourt.domain.api;

import com.hexagonal.ms_foodcourt.domain.model.Dish;
import org.springframework.data.domain.Page;

public interface IDishServicePort {

    void saveDish(Dish dish);

    void updateDish(Dish dish);

    void toggleStatusDish(Dish dish);

    Page<Dish> getListDish(Long restaurantId, Long categoryId, int page, int size);
}
