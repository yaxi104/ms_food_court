package com.hexagonal.ms_foodcourt.application.mapper;

import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRestaurantRequestMapper {

    Restaurant toRestaurant(RestaurantRequest restaurantRequest);

}
