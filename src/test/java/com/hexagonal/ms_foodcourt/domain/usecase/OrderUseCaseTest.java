package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;
import com.hexagonal.ms_foodcourt.domain.exception.DishNotRestaurantException;
import com.hexagonal.ms_foodcourt.domain.exception.OrdenByIdClientExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import com.hexagonal.ms_foodcourt.domain.model.User;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
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
}