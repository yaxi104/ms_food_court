package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.mapper;

import com.hexagonal.ms_foodcourt.domain.model.request.Restaurant;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantEntityMapper {

    RestaurantEntity toEntity(Restaurant restaurant);

    Restaurant toRestaurant(RestaurantEntity restaurantEntity);

}
