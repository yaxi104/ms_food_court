package com.hexagonal.ms_foodcourt.domain.usecase.order;

import com.hexagonal.ms_foodcourt.domain.api.order.IOrderReadyServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.OrderNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.OrderStatusNotReadyException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.OrderReadyEvent;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IPinSecurityPort;
import com.hexagonal.ms_foodcourt.domain.spi.ISqsSenderServicePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.domain.utils.DateHelper;
import com.hexagonal.ms_foodcourt.domain.utils.UserValidate;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

import java.util.Objects;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.LISTO;

public class OrderReadyUseCase implements IOrderReadyServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IUserFeignPort userFeignPort;
    private final IUserSessionPort userSessionPort;
    private final IPinSecurityPort pinSecurityPort;
    private final ISqsSenderServicePort sqsSenderServicePort;

    public OrderReadyUseCase(IOrderPersistencePort orderPersistencePort,
                             IUserFeignPort userFeignPort,
                             IUserSessionPort userSessionPort,
                             IPinSecurityPort pinSecurityPort,
                             ISqsSenderServicePort sqsSenderServicePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.userFeignPort = userFeignPort;
        this.userSessionPort = userSessionPort;
        this.pinSecurityPort = pinSecurityPort;
        this.sqsSenderServicePort = sqsSenderServicePort;
    }

    @Override
    public void markOrderAsReady(Long idOrder) {
        ValidateRequest.checkId(idOrder);
        Order order = orderPersistencePort.findById(idOrder).orElseThrow(OrderNotFoundException::new);
        if (!Objects.equals(order.getStatus(), EN_PREPARACION)) {
            throw new OrderStatusNotReadyException();
        }
        UserValidate.checkEmployee(order.getIdRestaurant(), userFeignPort, userSessionPort);
        User userCustomer = userFeignPort.getUserByid(order.getIdClient()).orElseThrow(UserNotExistsException::new);
        String pin = pinSecurityPort.getPin();
        String hashPin = pinSecurityPort.getHashPin(pin);
        order.setDate(DateHelper.dateBogota());
        order.setStatus(LISTO);
        order.setPin(hashPin);

        String message = String.format("Hola %s, PIN pedido #%d: %s",
                userCustomer.getFirstName(), idOrder, pin);

        OrderReadyEvent orderReadyEvent = new OrderReadyEvent();
        orderReadyEvent.setPhoneNumber(userCustomer.getPhoneNumber());
        orderReadyEvent.setMessage(message);
        sqsSenderServicePort.sendMessage(orderReadyEvent);
        orderPersistencePort.saveOrder(order);
    }

}
