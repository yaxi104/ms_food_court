package com.hexagonal.ms_foodcourt.domain.usecase.order;

import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.CANCELADO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderCanceledUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IUserFeignPort userFeignPort;

    @Mock
    private IUserSessionPort userSessionPort;

    private OrderCanceledUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new OrderCanceledUseCase(orderPersistencePort, userFeignPort, userSessionPort);
    }

    @Test
    void shouldCancelOrderSuccessfully() {
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setIdClient(100L);
        order.setStatus(PENDIENTE);

        User user = new User();
        user.setId(100L);

        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("client@test.com");
        when(userFeignPort.getUserByEmail("client@test.com")).thenReturn(Optional.of(user));

        MessageResult result = useCase.markOrderAsCanceled(orderId);

        assertEquals("Your order has been canceled", result.getMessage());
        assertEquals(CANCELADO, order.getStatus());
        verify(orderPersistencePort).saveOrder(order);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenIdIsInvalid() {
        Long invalidId = 0L;
        assertThrows(BadRequestException.class, () -> useCase.markOrderAsCanceled(invalidId));
    }

    @Test
    void shouldThrowOrderNotFoundException() {
        Long orderId = 99L;
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> useCase.markOrderAsCanceled(orderId));
    }

    @Test
    void shouldThrowUserNotExistsException() {
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setIdClient(100L);
        order.setStatus(PENDIENTE);

        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("client@test.com");
        when(userFeignPort.getUserByEmail("client@test.com")).thenReturn(Optional.empty());

        assertThrows(UserNotExistsException.class, () -> useCase.markOrderAsCanceled(orderId));
    }

    @Test
    void shouldThrowUserForbiddenExceptionWhenClientDoesNotMatch() {
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setIdClient(100L);
        order.setStatus(PENDIENTE);

        User user = new User();
        user.setId(200L);

        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("client@test.com");
        when(userFeignPort.getUserByEmail("client@test.com")).thenReturn(Optional.of(user));

        assertThrows(UserForbiddenException.class, () -> useCase.markOrderAsCanceled(orderId));
    }

    @Test
    void shouldThrowOrderStatusNotCanceledExceptionWhenStatusIsNotPending() {
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);
        order.setIdClient(100L);
        order.setStatus(EN_PREPARACION);

        User user = new User();
        user.setId(100L);

        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));
        when(userSessionPort.getCurrentUserEmail()).thenReturn("client@test.com");
        when(userFeignPort.getUserByEmail("client@test.com")).thenReturn(Optional.of(user));

        assertThrows(OrderStatusNotCanceledException.class, () -> useCase.markOrderAsCanceled(orderId));
    }
}