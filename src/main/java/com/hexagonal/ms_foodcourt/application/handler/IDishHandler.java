package com.hexagonal.ms_foodcourt.application.handler;


import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.DishResponse;

public interface IDishHandler {

    void saveDish(DishRequest dishRequest);

    void updateDish(DishUpdateRequest dishUpdateRequest);

    void toggleDishStatus(DishToggleStatusRequest dishToggleStatusRequest);

    PaginatedResponse<DishResponse> getListDishes(Long restaurantId, Long categoryId, Integer page, Integer size);

}
