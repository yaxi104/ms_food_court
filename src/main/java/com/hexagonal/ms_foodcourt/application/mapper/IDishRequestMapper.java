package com.hexagonal.ms_foodcourt.application.mapper;


import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.DishResponse;
import com.hexagonal.ms_foodcourt.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IDishRequestMapper {

    Dish toDish(DishRequest dishRequest);

    Dish toDishUpdate(DishUpdateRequest dishUpdateRequest);

    Dish toDishToggleStatus(DishToggleStatusRequest dishToggleStatusRequest);

    DishResponse toDishResponse(Dish dish);
}
