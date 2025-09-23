package com.hexagonal.ms_foodcourt.application.handler;


import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;

public interface IOrderHandler {

    void saveOrder(OrderRequest orderRequest);

}
