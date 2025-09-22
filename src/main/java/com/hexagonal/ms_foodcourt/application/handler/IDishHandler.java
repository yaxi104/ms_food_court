package com.hexagonal.ms_foodcourt.application.handler;


import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.DishResponse;
import org.springframework.data.domain.Page;

public interface IDishHandler {

    void saveDish(DishRequest dishRequest);

    void updateDish(DishUpdateRequest dishUpdateRequest);

    void toggleDishStatus(DishToggleStatusRequest dishToggleStatusRequest);

    Page<DishResponse> getListRestaurants(Long restaurantId, Long categoryId, int page, int size);

}
