package com.hexagonal.ms_foodcourt.domain.api;

import com.hexagonal.ms_foodcourt.domain.model.OrderReq;

public interface IOrderServicePort {

    void saveOrder(OrderReq orderReq);
}
