package com.hexagonal.ms_foodcourt.application.mapper;


import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderRequestMapper {

    @Mapping(target = "orderDishList", source = "orderDishRequests")
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "status", ignore = true)
    OrderReq toOrderReq(OrderRequest orderRequest);

}
