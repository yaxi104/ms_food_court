package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.mapper;

import com.hexagonal.ms_foodcourt.domain.model.Dish;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishEntityMapper {

    DishEntity toEntity(Dish restaurant);

    Dish toDish(DishEntity dishEntity);

    List<Dish> toDishList(List<DishEntity> dishEntityList);

}
