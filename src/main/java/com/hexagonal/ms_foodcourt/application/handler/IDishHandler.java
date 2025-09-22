package com.hexagonal.ms_foodcourt.application.handler;


import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;

public interface IDishHandler {

    void saveDish(DishRequest dishRequest);

    void updateDish(DishUpdateRequest dishUpdateRequest);
}
