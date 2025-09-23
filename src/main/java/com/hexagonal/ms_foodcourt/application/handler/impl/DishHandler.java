package com.hexagonal.ms_foodcourt.application.handler.impl;


import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.DishResponse;
import com.hexagonal.ms_foodcourt.application.handler.IDishHandler;
import com.hexagonal.ms_foodcourt.application.mapper.IDishRequestMapper;
import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.domain.model.PageResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;

    @Override
    public void saveDish(DishRequest dishRequest) {
        dishServicePort.saveDish(dishRequestMapper.toDish(dishRequest));
    }

    @Override
    public void updateDish(DishUpdateRequest dishUpdateRequest) {
        dishServicePort.updateDish(dishRequestMapper.toDishUpdate(dishUpdateRequest));
    }

    @Override
    public void toggleDishStatus(DishToggleStatusRequest dishToggleStatusRequest) {
        dishServicePort.toggleStatusDish(dishRequestMapper.toDishToggleStatus(dishToggleStatusRequest));
    }

    @Override
    public PaginatedResponse<DishResponse> getListDishes(Long restaurantId, Long categoryId, Integer page, Integer size) {
        PageResult<Dish> resultPage = dishServicePort.getListDish(restaurantId, categoryId, page, size);

        List<DishResponse> responseList = resultPage.getContent().stream()
                .map(dishRequestMapper::toDishResponse)
                .toList();

        return new PaginatedResponse<>(
                responseList,
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.isLast()
        );
    }

}
