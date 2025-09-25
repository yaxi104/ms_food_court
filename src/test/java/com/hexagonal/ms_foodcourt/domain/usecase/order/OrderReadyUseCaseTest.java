package com.hexagonal.ms_foodcourt.domain.usecase.order;

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
import com.hexagonal.ms_foodcourt.util.TestDataOrderFactory;
import com.hexagonal.ms_foodcourt.util.TestDataUserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ENTREGADO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.LISTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderReadyUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IUserFeignPort userFeignPort;

    @Mock
    private IUserSessionPort userSessionPort;

    @Mock
    private IPinSecurityPort pinSecurityPort;

    @Mock
    private ISqsSenderServicePort sqsSenderServicePort;

    private OrderReadyUseCase orderReadyUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderReadyUseCase = new OrderReadyUseCase(orderPersistencePort, userFeignPort, userSessionPort, pinSecurityPort, sqsSenderServicePort);
    }

    @Test
    void markOrderAsReadySuccessTest() {
        Order order = TestDataOrderFactory.mockOrder();
        order.setStatus(EN_PREPARACION);

        User clientUser = TestDataUserFactory.mockUser();
        order.setIdClient(clientUser.getId());

        String employeeEmail = "test@example.com";
        User employeeUser = TestDataUserFactory.mockUser();
        employeeUser.setEmail(employeeEmail);
        employeeUser.setRestaurantId(order.getIdRestaurant());

        String pin = "4321";
        String hashedPin = "hashed_4321";

        when(userSessionPort.getCurrentUserEmail()).thenReturn(employeeEmail);
        when(userFeignPort.getUserByEmail(employeeEmail)).thenReturn(Optional.of(employeeUser));

        when(userFeignPort.getUserByid(clientUser.getId())).thenReturn(Optional.of(clientUser));

        when(orderPersistencePort.findById(order.getId())).thenReturn(Optional.of(order));
        when(pinSecurityPort.getPin()).thenReturn(pin);
        when(pinSecurityPort.getHashPin(pin)).thenReturn(hashedPin);

        orderReadyUseCase.markOrderAsReady(order.getId());

        assertEquals(LISTO, order.getStatus());
        assertEquals(hashedPin, order.getPin());
        assertNotNull(order.getDate());

        ArgumentCaptor<OrderReadyEvent> captor = ArgumentCaptor.forClass(OrderReadyEvent.class);
        verify(sqsSenderServicePort).sendMessage(captor.capture());

        OrderReadyEvent event = captor.getValue();
        assertEquals(clientUser.getPhoneNumber(), event.getPhoneNumber());
        assertTrue(event.getMessage().contains(pin));

        verify(orderPersistencePort).saveOrder(order);
    }

    @Test
    void markOrderAsReadyOrderNotFoundExceptionTest() {
        when(orderPersistencePort.findById(123L)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderReadyUseCase.markOrderAsReady(123L));
    }

    @Test
    void markOrderAsReadyStatusNotReadyExceptionTest() {
        Order order = TestDataOrderFactory.mockOrder();
        order.setStatus(ENTREGADO);

        when(orderPersistencePort.findById(order.getId())).thenReturn(Optional.of(order));

        Executable executable = () -> orderReadyUseCase.markOrderAsReady(order.getId());

        assertThrows(OrderStatusNotReadyException.class, executable);
    }

    @Test
    void markOrderAsReadyUserNotExistsExceptionTest() {
        Order order = TestDataOrderFactory.mockOrder();
        order.setStatus(EN_PREPARACION);

        when(orderPersistencePort.findById(order.getId())).thenReturn(Optional.of(order));
        when(userFeignPort.getUserByid(order.getIdClient())).thenReturn(Optional.empty());

        Executable executable = () -> orderReadyUseCase.markOrderAsReady(order.getId());

        assertThrows(UserNotExistsException.class, executable);
    }

}