package com.hexagonal.ms_foodcourt.domain.api;

import com.hexagonal.ms_foodcourt.domain.model.Dish;

public interface IDishServicePort {

    void saveDish(Dish dish);

    void updateDish(Dish dish);

    void toggleStatusDish(Dish dish);

}
