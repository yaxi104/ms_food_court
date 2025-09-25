package com.hexagonal.ms_foodcourt.domain.usecase.order;

import com.hexagonal.ms_foodcourt.domain.exception.OrderNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.OrderStatusNotDelirevedException;
import com.hexagonal.ms_foodcourt.domain.exception.PinIncorrectException;
import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.model.DeliverOrder;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IPinSecurityPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.util.TestDataOrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ENTREGADO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.LISTO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderDeliveredUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IUserFeignPort userFeignPort;

    @Mock
    private IUserSessionPort userSessionPort;

    @Mock
    private IPinSecurityPort pinSecurityPort;

    private OrderDeliveredUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new OrderDeliveredUseCase(orderPersistencePort, userFeignPort, userSessionPort, pinSecurityPort);
    }

    @Test
    void shouldDeliverOrderSuccessfully() {
        DeliverOrder request = TestDataOrderFactory.mockDeliverOrder();

        Order order = new Order();
        order.setId(1L);
        order.setStatus(LISTO);
        order.setPin("1234");
        order.setIdRestaurant(10L);

        User employee = new User();
        employee.setRestaurantId(10L);

        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("empleado@test.com");
        when(userFeignPort.getUserByEmail("empleado@test.com")).thenReturn(Optional.of(employee));
        when(pinSecurityPort.checkPin("1234", "1234")).thenReturn(true);

        useCase.markOrderAsDelivered(request);

        assertEquals(ENTREGADO, order.getStatus());
        verify(orderPersistencePort).saveOrder(order);
    }


    @Test
    void shouldThrowOrderNotFoundException() {
        DeliverOrder request = TestDataOrderFactory.mockDeliverOrder();
        when(orderPersistencePort.findById(1L)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> useCase.markOrderAsDelivered(request));
    }

    @Test
    void shouldThrowPinIncorrectException() {
        DeliverOrder request = new DeliverOrder();
        request.setOrderId(1L);
        request.setPin("1234");

        Order order = new Order();
        order.setStatus(PENDIENTE);
        order.setPin("1234");
        order.setIdRestaurant(10L);

        User employee = new User();
        employee.setRestaurantId(10L);

        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("empleado@test.com");
        when(userFeignPort.getUserByEmail("empleado@test.com")).thenReturn(Optional.of(employee));

        assertThrows(OrderStatusNotDelirevedException.class, () -> useCase.markOrderAsDelivered(request));
    }

    @Test
    void shouldThrowStatusIncorrectException() {
        DeliverOrder request = new DeliverOrder();
        request.setOrderId(1L);
        request.setPin("wrong-pin");

        Order order = new Order();
        order.setStatus(LISTO);
        order.setPin("1234");
        order.setIdRestaurant(10L);

        User employee = new User();
        employee.setRestaurantId(10L);

        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("empleado@test.com");
        when(userFeignPort.getUserByEmail("empleado@test.com")).thenReturn(Optional.of(employee));
        when(pinSecurityPort.checkPin("wrong-pin", "1234")).thenReturn(false);

        assertThrows(PinIncorrectException.class, () -> useCase.markOrderAsDelivered(request));
    }


    @Test
    void shouldThrowUserForbiddenException() {
        DeliverOrder request = TestDataOrderFactory.mockDeliverOrder();

        Order order = new Order();
        order.setStatus(LISTO);
        order.setPin("1234");
        order.setIdRestaurant(10L);

        User employee = new User();
        employee.setRestaurantId(99L);

        when(orderPersistencePort.findById(1L)).thenReturn(Optional.of(order));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("empleado@test.com");
        when(userFeignPort.getUserByEmail("empleado@test.com")).thenReturn(Optional.of(employee));

        assertThrows(UserForbiddenException.class, () -> useCase.markOrderAsDelivered(request));
    }
}