package com.hexagonal.ms_foodcourt.domain.usecase.order;

import com.hexagonal.ms_foodcourt.domain.api.order.IOrderDeliveredServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.OrderNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.OrderStatusNotDelirevedException;
import com.hexagonal.ms_foodcourt.domain.exception.PinIncorrectException;
import com.hexagonal.ms_foodcourt.domain.model.DeliverOrder;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.Traceability;
import com.hexagonal.ms_foodcourt.domain.model.response.MessageResult;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IPinSecurityPort;
import com.hexagonal.ms_foodcourt.domain.spi.ITraceFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.domain.utils.DateHelper;
import com.hexagonal.ms_foodcourt.domain.utils.TraceabilityHelper;
import com.hexagonal.ms_foodcourt.domain.utils.UserValidate;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

import java.util.Objects;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ENTREGADO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.LISTO;

public class OrderDeliveredUseCase implements IOrderDeliveredServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserFeignPort userFeignPort;
    private final IUserSessionPort userSessionPort;
    private final IPinSecurityPort pinSecurityPort;
    private final ITraceFeignPort traceFeignPort;

    public OrderDeliveredUseCase(IOrderPersistencePort orderPersistencePort,
                                 IUserFeignPort userFeignPort,
                                 IUserSessionPort userSessionPort,
                                 IPinSecurityPort pinSecurityPort,
                                 ITraceFeignPort traceFeignPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.userFeignPort = userFeignPort;
        this.userSessionPort = userSessionPort;
        this.pinSecurityPort = pinSecurityPort;
        this.traceFeignPort = traceFeignPort;
    }

    @Override
    public MessageResult markOrderAsDelivered(DeliverOrder deliverOrderRequest) {
        Long orderId = deliverOrderRequest.getOrderId();
        ValidateRequest.checkId(orderId);
        ValidateRequest.checkNotBlank(deliverOrderRequest.getPin());

        Order order = orderPersistencePort.findById(orderId).orElseThrow(OrderNotFoundException::new);
        String statusPrevious = order.getStatus();

        UserValidate.checkEmployee(order.getIdRestaurant(), userFeignPort, userSessionPort);

        if (!Objects.equals(order.getStatus(), LISTO)) {
            throw new OrderStatusNotDelirevedException();
        }

        if (!pinSecurityPort.checkPin(deliverOrderRequest.getPin(), order.getPin())) {
            throw new PinIncorrectException();
        }

        order.setDate(DateHelper.dateBogota());
        order.setStatus(ENTREGADO);
        orderPersistencePort.saveOrder(order);
        Traceability traceability = TraceabilityHelper.createTrace(order, statusPrevious, userFeignPort);
        traceFeignPort.saveTraceability(traceability);
        return new MessageResult("Your order has been delivered");

    }
}
