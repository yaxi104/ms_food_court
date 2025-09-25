package com.hexagonal.ms_foodcourt.domain.api.order;

import com.hexagonal.ms_foodcourt.domain.model.response.OrderResult;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;

public interface IOrderGetListServicePort {

    PageResult<OrderResult> getAllOrderByStatus(String status, Long idRestaurant, Integer page, Integer size);

}
