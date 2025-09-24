package com.hexagonal.ms_foodcourt.application.mapper;

import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.RestaurantResponse;
import com.hexagonal.ms_foodcourt.domain.model.Restaurant;
import com.hexagonal.ms_foodcourt.domain.model.response.RestaurantResult;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRestaurantMapper {

    Restaurant toRestaurant(RestaurantRequest restaurantRequest);

    RestaurantResponse toRestaurantResponse(RestaurantResult restaurantResult);

    List<RestaurantResponse> toRestaurantResponseList(List<RestaurantResult> restaurantResult);

}
