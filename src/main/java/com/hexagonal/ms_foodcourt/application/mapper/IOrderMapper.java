package com.hexagonal.ms_foodcourt.application.mapper;


import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.OrderResponse;
import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderMapper {

    @Mapping(target = "orderDishList", source = "orderDishRequests")
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "status", ignore = true)
    OrderReq toOrderReq(OrderRequest orderRequest);

    OrderResponse toOrderResponse(OrderResult orderResult);

    List<OrderResponse> toOrderResponseList(List<OrderResult> orderResult);
}
