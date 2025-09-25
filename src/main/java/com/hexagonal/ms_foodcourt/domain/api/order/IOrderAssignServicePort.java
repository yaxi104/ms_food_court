package com.hexagonal.ms_foodcourt.domain.api.order;

import com.hexagonal.ms_foodcourt.domain.model.response.MessageResult;

public interface IOrderAssignServicePort {

    MessageResult assignOrderToEmployee(Long orderId);

}
