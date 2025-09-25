package com.hexagonal.ms_foodcourt.domain.usecase.order;

import com.hexagonal.ms_foodcourt.domain.api.order.IOrderGetListServicePort;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderDishResult;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderResult;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.domain.utils.PageableHelper;
import com.hexagonal.ms_foodcourt.domain.utils.UserValidate;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

import java.util.Collections;
import java.util.List;

public class OrderGetListUseCase implements IOrderGetListServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IUserFeignPort userFeignPort;
    private final IUserSessionPort userSessionPort;

    public OrderGetListUseCase(IOrderPersistencePort orderPersistencePort,
                               IOrderDishPersistencePort orderDishPersistencePort,
                               IUserFeignPort userFeignPort,
                               IUserSessionPort userSessionPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.userFeignPort = userFeignPort;
        this.userSessionPort = userSessionPort;
    }


    @Override
    public PageResult<OrderResult> getAllOrderByStatus(String status, Long idRestaurant, Integer page, Integer size) {
        ValidateRequest.checkStatusOrderValid(status);
        ValidateRequest.checkId(idRestaurant);
        UserValidate.checkEmployee(idRestaurant, userFeignPort, userSessionPort);

        PageInfo pageInfo = PageableHelper.getPageable(page, size, "date");
        PageResult<OrderResult> orderPage = orderPersistencePort.findByStatusAndIdRestaurant(status, idRestaurant, pageInfo);
        List<OrderResult> orderList = orderPage.getContent();
        if (orderList.isEmpty()) {
            return new PageResult<>(Collections.emptyList(), 0, 0, false);
        }

        List<OrderResult> orderResults = orderList.stream().map(this::getOrderResult).toList();
        orderPage.setContent(orderResults);
        return orderPage;
    }


    private OrderResult getOrderResult(OrderResult orderResult) {
        List<OrderDishResult> orderDishResults = orderDishPersistencePort.findAllByIdOrderWithNames(orderResult.getId());
        orderResult.setOrderDishResponses(orderDishResults);
        return orderResult;
    }

}
