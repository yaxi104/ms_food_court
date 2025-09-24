package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.mapper;

import com.hexagonal.ms_foodcourt.domain.model.OrderDish;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.entity.OrderDishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderDishEntityMapper {

    OrderDishEntity toEntity(OrderDish order);

    OrderDish toOrderDish(OrderDishEntity dishEntity);

    List<OrderDish> toOrderDishList(List<OrderDishEntity> dishEntityList);

    List<OrderDishEntity> toOrderDishEntityList(List<OrderDish> dishEntityList);

}
