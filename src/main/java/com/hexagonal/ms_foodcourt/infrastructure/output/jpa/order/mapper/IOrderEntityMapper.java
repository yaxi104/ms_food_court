package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.mapper;

import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {

    OrderEntity toEntity(Order order);

    Order toOrder(OrderEntity dishEntity);

}
