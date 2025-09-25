package com.hexagonal.ms_foodcourt.domain.usecase.order;

import com.hexagonal.ms_foodcourt.domain.api.order.IOrderAssignServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.OrderNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.OrderStatusNotAssignedException;
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

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;

public class OrderAssignUseCase implements IOrderAssignServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserFeignPort userFeignPort;
    private final IUserSessionPort userSessionPort;

    public OrderAssignUseCase(IOrderPersistencePort orderPersistencePort,
                              IUserFeignPort userFeignPort,
                              IUserSessionPort userSessionPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.userFeignPort = userFeignPort;
        this.userSessionPort = userSessionPort;
    }

    @Override
    public MessageResult assignOrderToEmployee(Long orderId) {
        ValidateRequest.checkId(orderId);
        User userEmployee = userFeignPort.getUserByEmail(userSessionPort.getCurrentUserEmail()).orElseThrow(UserNotExistsException::new);

        Order order = orderPersistencePort.findById(orderId).orElseThrow(OrderNotFoundException::new);

        if (!Objects.equals(userEmployee.getRestaurantId(), order.getIdRestaurant())) {
            throw new UserForbiddenException();
        }

        if (!Objects.equals(order.getStatus(), PENDIENTE)) {
            throw new OrderStatusNotAssignedException();
        }
        order.setIdChef(userEmployee.getId());
        order.setStatus(EN_PREPARACION);
        order.setDate(DateHelper.dateBogota());
        orderPersistencePort.saveOrder(order);
        return new MessageResult("Order has been successfully assigned");
    }

}
