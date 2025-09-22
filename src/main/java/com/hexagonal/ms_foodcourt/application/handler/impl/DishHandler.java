package com.hexagonal.ms_foodcourt.application.handler.impl;


import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.handler.IDishHandler;
import com.hexagonal.ms_foodcourt.application.mapper.IDishRequestMapper;
import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
