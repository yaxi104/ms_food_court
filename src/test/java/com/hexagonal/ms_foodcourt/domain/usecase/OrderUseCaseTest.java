package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;
import com.hexagonal.ms_foodcourt.domain.exception.DishNotRestaurantException;
import com.hexagonal.ms_foodcourt.domain.exception.OrdenByIdClientExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.OrderNotFoundException;
import com.hexagonal.ms_foodcourt.domain.exception.OrderStatusNotAssignedException;
import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import com.hexagonal.ms_foodcourt.domain.model.PageInfo;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderDishResult;
import com.hexagonal.ms_foodcourt.domain.model.response.OrderResult;
import com.hexagonal.ms_foodcourt.domain.model.response.PageResult;
import com.hexagonal.ms_foodcourt.domain.spi.IDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.util.TestDataOrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;
    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private IOrderDishPersistencePort orderDishPersistencePort;
    @Mock
    private IUserFeignPort userFeignPort;
    @Mock
    private IUserSessionPort userSessionPort;

    private OrderUseCase orderUseCase;

    private static final String EMAIL_TEST = "employee@example.com";

    @BeforeEach
    void setUp() {
        orderUseCase = new OrderUseCase(
                dishPersistencePort,
                orderPersistencePort,
                orderDishPersistencePort,
                userFeignPort,
                userSessionPort
        );
    }

    @Test
    void saveOrderSuccess() {
        String email = "cliente@correo.com";
        Long clientId = 1L;
        OrderReq orderReq = TestDataOrderFactory.mockOrderReq();
        List<Long> dishIds = List.of(1L);

        User user = new User();
        user.setId(clientId);

        when(userSessionPort.getCurrentUserEmail()).thenReturn(email);
        when(userFeignPort.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(orderPersistencePort.existsByIdClientAndStatusList(eq(clientId), anyList())).thenReturn(false);
        when(dishPersistencePort.countValidDishesByRestaurant((dishIds), (orderReq.getIdRestaurant()))).thenReturn(1L);
        when(orderPersistencePort.saveOrder(any(Order.class))).thenReturn(123L);

        orderUseCase.saveOrder(orderReq);

        verify(orderPersistencePort).saveOrder(any(Order.class));
        verify(orderDishPersistencePort).saveAllOrderDish(anyList());
    }

    @Test
    void saveOrderSuccessBadRequest() {
        OrderReq orderReq = TestDataOrderFactory.mockOrderReq();
        orderReq.setOrderDishList(null);
        assertThrows(BadRequestException.class, () -> orderUseCase.saveOrder(orderReq));
    }

    @Test
    void saveOrderSuccessEmptyBadRequest() {
        OrderReq orderReq = TestDataOrderFactory.mockOrderReq();
        orderReq.setOrderDishList(List.of());
        assertThrows(BadRequestException.class, () -> orderUseCase.saveOrder(orderReq));
    }

    @Test
    void saveOrderThrowsWhenUserDoesNotExist() {
        String email = "cliente@correo.com";

        when(userSessionPort.getCurrentUserEmail()).thenReturn(email);
        when(userFeignPort.getUserByEmail(email)).thenReturn(Optional.empty());

        OrderReq orderReq = TestDataOrderFactory.mockOrderReq();

        assertThrows(UserNotExistsException.class, () -> orderUseCase.saveOrder(orderReq));
    }

    @Test
    void saveOrderThrowsWhenClientHasActiveOrder() {
        String email = "cliente@correo.com";
        Long clientId = 1L;

        User user = new User();
        user.setId(clientId);

        when(userSessionPort.getCurrentUserEmail()).thenReturn(email);
        when(userFeignPort.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(orderPersistencePort.existsByIdClientAndStatusList(eq(clientId), anyList())).thenReturn(true);

        OrderReq orderReq = TestDataOrderFactory.mockOrderReq();

        assertThrows(OrdenByIdClientExistsException.class, () -> orderUseCase.saveOrder(orderReq));
    }

    @Test
    void saveOrderThrowsWhenDishesNotFromRestaurant() {
        String email = "cliente@correo.com";
        Long clientId = 1L;
        OrderReq orderReq = TestDataOrderFactory.mockOrderReq();
        List<Long> dishIds = List.of(1L);

        User user = new User();
        user.setId(clientId);

        when(userSessionPort.getCurrentUserEmail()).thenReturn(email);
        when(userFeignPort.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(orderPersistencePort.existsByIdClientAndStatusList(eq(clientId), anyList())).thenReturn(false);
        when(dishPersistencePort.countValidDishesByRestaurant((dishIds), (orderReq.getIdRestaurant()))).thenReturn(0L);

        assertThrows(DishNotRestaurantException.class, () -> {
            orderUseCase.saveOrder(orderReq);
        });
    }

    @Test
    void getAllOrderByStatusSuccess() {
        String status = "PENDIENTE";
        Long restaurantId = 1L;
        Integer page = 0;
        Integer size = 2;

        String userEmail = "employee@example.com";
        when(userSessionPort.getCurrentUserEmail()).thenReturn(userEmail);

        User user = new User();
        user.setEmail(userEmail);
        user.setRestaurantId(restaurantId);
        when(userFeignPort.getUserByEmail(userEmail)).thenReturn(Optional.of(user));

        OrderResult order1 = new OrderResult();
        order1.setId(101L);
        order1.setStatus(status);

        OrderResult order2 = new OrderResult();
        order2.setId(102L);
        order2.setStatus(status);

        List<OrderResult> orderList = List.of(order1, order2);
        PageResult<OrderResult> mockPage = new PageResult<>(orderList, 1, 2, true);
        when(orderPersistencePort.findByStatusAndIdRestaurant(eq(status), eq(restaurantId), any(PageInfo.class)))
                .thenReturn(mockPage);

        List<OrderDishResult> dishResults = List.of(
                new OrderDishResult(1L, "Dish A", 2),
                new OrderDishResult(2L, "Dish B", 1)
        );
        when(orderDishPersistencePort.findAllByIdOrderWithNames(anyLong())).thenReturn(dishResults);

        PageResult<OrderResult> result = orderUseCase.getAllOrderByStatus(status, restaurantId, page, size);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getTotalElements());
        assertTrue(result.isLast());

        for (OrderResult order : result.getContent()) {
            assertEquals(dishResults, order.getOrderDishResponses());
        }

        verify(userSessionPort).getCurrentUserEmail();
        verify(userFeignPort).getUserByEmail(userEmail);
        verify(orderPersistencePort).findByStatusAndIdRestaurant(eq(status), eq(restaurantId), any(PageInfo.class));
        verify(orderDishPersistencePort, times(2)).findAllByIdOrderWithNames(anyLong());
    }

    @Test
    void getAllOrderByStatusWhenUserNotFound() {
        String status = "PENDIENTE";
        Long restaurantId = 1L;

        when(userSessionPort.getCurrentUserEmail()).thenReturn("missing@example.com");
        when(userFeignPort.getUserByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotExistsException.class, () ->
                orderUseCase.getAllOrderByStatus(status, restaurantId, 0, 10)
        );
    }


    @Test
    void getAllOrderByStatusWhenUserFromAnotherRestaurant() {
        String status = "PENDIENTE";
        Long correctRestaurantId = 1L;
        Long otherRestaurantId = 999L;

        when(userSessionPort.getCurrentUserEmail()).thenReturn("user@other.com");

        User user = new User();
        user.setRestaurantId(otherRestaurantId);
        when(userFeignPort.getUserByEmail(anyString())).thenReturn(Optional.of(user));

        assertThrows(UserForbiddenException.class, () ->
                orderUseCase.getAllOrderByStatus(status, correctRestaurantId, 0, 10)
        );
    }

    @Test
    void getAllOrderByStatusWithEmptyOrders() {
        String status = "PENDIENTE";
        Long restaurantId = 1L;

        String email = "employee@example.com";
        when(userSessionPort.getCurrentUserEmail()).thenReturn(email);

        User user = new User();
        user.setEmail(email);
        user.setRestaurantId(restaurantId);
        when(userFeignPort.getUserByEmail(email)).thenReturn(Optional.of(user));

        PageResult<OrderResult> emptyPage = new PageResult<>(Collections.emptyList(), 0, 0, false);
        when(orderPersistencePort.findByStatusAndIdRestaurant(eq(status), eq(restaurantId), any(PageInfo.class)))
                .thenReturn(emptyPage);

        PageResult<OrderResult> result = orderUseCase.getAllOrderByStatus(status, restaurantId, 0, 10);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getTotalElements());
        assertFalse(result.isLast());
    }

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

        orderUseCase.assignOrderToEmployee(orderId);

        assertEquals(employeeId, order.getIdChef());
        assertEquals(EN_PREPARACION, order.getStatus());
        assertNotNull(order.getDate());

        verify(orderPersistencePort).saveOrder(order);
    }

    @Test
    void assignOrderToEmployeeThrowsWhenOrderIdInvalid() {
        Long invalidOrderId = 0L;

        assertThrows(BadRequestException.class, () -> orderUseCase.assignOrderToEmployee(invalidOrderId));
    }

    @Test
    void assignOrderToEmployeeThrowsWhenUserNotFound() {
        Long orderId = 1L;

        when(userSessionPort.getCurrentUserEmail()).thenReturn(EMAIL_TEST);
        when(userFeignPort.getUserByEmail(EMAIL_TEST)).thenReturn(Optional.empty());

        assertThrows(UserNotExistsException.class, () -> orderUseCase.assignOrderToEmployee(orderId));
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

        assertThrows(OrderNotFoundException.class, () -> orderUseCase.assignOrderToEmployee(orderId));
    }

    @Test
    void assignOrderToEmployeeThrowsWhenOrderStatusNotPendiente() {
        Long orderId = 1L;

        User userEmployee = new User();
        userEmployee.setId(10L);
        userEmployee.setEmail(EMAIL_TEST);

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(EN_PREPARACION); // No es PENDIENTE, debe lanzar excepción

        when(userSessionPort.getCurrentUserEmail()).thenReturn(EMAIL_TEST);
        when(userFeignPort.getUserByEmail(EMAIL_TEST)).thenReturn(Optional.of(userEmployee));
        when(orderPersistencePort.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(OrderStatusNotAssignedException.class, () -> orderUseCase.assignOrderToEmployee(orderId));
    }

}
