package com.hexagonal.ms_foodcourt.domain.api.order;

import com.hexagonal.ms_foodcourt.domain.model.OrderReq;

public interface IOrderSaveServicePort {

    void saveOrder(OrderReq orderReq);
}
