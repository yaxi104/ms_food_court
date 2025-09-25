package com.hexagonal.ms_foodcourt.domain.usecase.order;

import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;
import com.hexagonal.ms_foodcourt.domain.exception.OrderNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.OrderStatusNotAssignedException;
import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderAssignUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private IUserFeignPort userFeignPort;
    @Mock
    private IUserSessionPort userSessionPort;
    @InjectMocks
    private OrderAssignUseCase orderAssignUseCase;

    private static final String EMAIL_TEST = "employee@example.com";

    @Test
    void assignOrderToEmployeeSuccess() {
        Long orderId = 1L;
        Long employeeId = 10L;

        User userEmployee = new User();
        userEmployee.setId(employeeId);
        userEmployee.setEmail(EMAIL_TEST);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(PENDIENTE);

        when(userSessionPort.getCurrentUserEmail()).thenReturn(EMAIL_TEST);
        when(userFeignPort.getUserByEmail(EMAIL_TEST)).thenReturn(Optional.of(userEmployee));
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));
        when(orderPersistencePort.saveOrder(any(Order.class))).thenReturn(orderId);

        orderAssignUseCase.assignOrderToEmployee(orderId);

        assertEquals(employeeId, order.getIdChef());
        assertEquals(EN_PREPARACION, order.getStatus());
        assertNotNull(order.getDate());

        verify(orderPersistencePort).saveOrder(order);
    }

    @Test
    void assignOrderToEmployeeThrowsWhenOrderIdInvalid() {
        Long invalidOrderId = 0L;

        assertThrows(BadRequestException.class, () -> orderAssignUseCase.assignOrderToEmployee(invalidOrderId));
    }

    @Test
    void assignOrderToEmployeeThrowsWhenUserNotFound() {
        Long orderId = 1L;

        when(userSessionPort.getCurrentUserEmail()).thenReturn(EMAIL_TEST);
        when(userFeignPort.getUserByEmail(EMAIL_TEST)).thenReturn(Optional.empty());

        assertThrows(UserNotExistsException.class, () -> orderAssignUseCase.assignOrderToEmployee(orderId));
    }

    @Test
    void assignOrderToEmployeeThrowsWhenOrderNotFound() {
        Long orderId = 1L;

        User userEmployee = new User();
        userEmployee.setId(10L);
        userEmployee.setEmail(EMAIL_TEST);

        when(userSessionPort.getCurrentUserEmail()).thenReturn(EMAIL_TEST);
        when(userFeignPort.getUserByEmail(EMAIL_TEST)).thenReturn(Optional.of(userEmployee));
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderAssignUseCase.assignOrderToEmployee(orderId));
    }

    @Test
    void assignOrderToEmployeeThrowsWhenOrderStatusNotPendiente() {
        Long orderId = 1L;

        User userEmployee = new User();
        userEmployee.setId(10L);
        userEmployee.setEmail(EMAIL_TEST);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(EN_PREPARACION);

        when(userSessionPort.getCurrentUserEmail()).thenReturn(EMAIL_TEST);
        when(userFeignPort.getUserByEmail(EMAIL_TEST)).thenReturn(Optional.of(userEmployee));
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderStatusNotAssignedException.class, () -> orderAssignUseCase.assignOrderToEmployee(orderId));
    }

    @Test
    void assignOrderToEmployeeValidateEmployeeTest() {
        Long orderId = 1L;

        User userEmployee = new User();
        userEmployee.setId(10L);
        userEmployee.setRestaurantId(1L);
        userEmployee.setEmail(EMAIL_TEST);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(EN_PREPARACION);
        userEmployee.setRestaurantId(2L);

        when(userSessionPort.getCurrentUserEmail()).thenReturn(EMAIL_TEST);
        when(userFeignPort.getUserByEmail(EMAIL_TEST)).thenReturn(Optional.of(userEmployee));
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(UserForbiddenException.class, () -> orderAssignUseCase.assignOrderToEmployee(orderId));
    }

}
