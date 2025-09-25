package com.hexagonal.ms_foodcourt.domain.usecase.order;

import com.hexagonal.ms_foodcourt.domain.api.order.IOrderCanceledServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.OrderNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.OrderStatusNotCanceledException;
import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.model.response.MessageResult;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.domain.utils.DateHelper;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

import java.util.Objects;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.CANCELADO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;

public class OrderCanceledUseCase implements IOrderCanceledServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserFeignPort userFeignPort;
    private final IUserSessionPort userSessionPort;

    public OrderCanceledUseCase(IOrderPersistencePort orderPersistencePort,
                                IUserFeignPort userFeignPort,
                                IUserSessionPort userSessionPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.userFeignPort = userFeignPort;
        this.userSessionPort = userSessionPort;
    }

    @Override
    public MessageResult markOrderAsCanceled(Long orderId) {
        ValidateRequest.checkId(orderId);
        Order order = orderPersistencePort.findById(orderId).orElseThrow(OrderNotFoundException::new);
        User userClient = userFeignPort.getUserByEmail(userSessionPort.getCurrentUserEmail()).orElseThrow(UserNotExistsException::new);
        if (!Objects.equals(userClient.getId(), order.getIdClient())) {
            throw new UserForbiddenException();
        }
        if (!Objects.equals(order.getStatus(), PENDIENTE)) {
            throw new OrderStatusNotCanceledException();
        }
        order.setDate(DateHelper.dateBogota());
        order.setStatus(CANCELADO);
        orderPersistencePort.saveOrder(order);
        return new MessageResult("Your order has been canceled");
    }
}
