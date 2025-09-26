package com.hexagonal.ms_foodcourt.domain.usecase.order;

import com.hexagonal.ms_foodcourt.domain.api.order.IOrderAssignServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.OrderNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.OrderStatusNotAssignedException;
import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.Traceability;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.model.response.MessageResult;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.ITraceFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.domain.utils.DateHelper;
import com.hexagonal.ms_foodcourt.domain.utils.TraceabilityHelper;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

import java.util.Objects;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;

public class OrderAssignUseCase implements IOrderAssignServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserFeignPort userFeignPort;
    private final IUserSessionPort userSessionPort;
    private final ITraceFeignPort traceFeignPort;

    public OrderAssignUseCase(IOrderPersistencePort orderPersistencePort,
                              IUserFeignPort userFeignPort,
                              IUserSessionPort userSessionPort,
                              ITraceFeignPort traceFeignPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.userFeignPort = userFeignPort;
        this.userSessionPort = userSessionPort;
        this.traceFeignPort = traceFeignPort;
    }

    @Override
    public MessageResult assignOrderToEmployee(Long orderId) {
        ValidateRequest.checkId(orderId);
        User userEmployee = userFeignPort.getUserByEmail(userSessionPort.getCurrentUserEmail()).orElseThrow(UserNotExistsException::new);

        Order order = orderPersistencePort.findById(orderId).orElseThrow(OrderNotFoundException::new);
        String statusPrevious = order.getStatus();
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
        Traceability traceability = TraceabilityHelper.createTrace(order, statusPrevious, userFeignPort);
        traceFeignPort.saveTraceability(traceability);
        return new MessageResult("Order has been successfully assigned");
    }

}
