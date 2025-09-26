package com.hexagonal.ms_foodcourt.domain.usecase.order;

import com.hexagonal.ms_foodcourt.domain.api.order.IOrderSaveServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;
import com.hexagonal.ms_foodcourt.domain.exception.DishNotRestaurantException;
import com.hexagonal.ms_foodcourt.domain.exception.OrdenByIdClientExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.OrderDish;
import com.hexagonal.ms_foodcourt.domain.model.OrderReq;
import com.hexagonal.ms_foodcourt.domain.model.Traceability;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.spi.IDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.ITraceFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.domain.utils.DateHelper;
import com.hexagonal.ms_foodcourt.domain.utils.TraceabilityHelper;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

import java.util.List;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.LISTO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;

public class OrderSaveUseCase implements IOrderSaveServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IUserFeignPort userFeignPort;
    private final IUserSessionPort userSessionPort;
    private final ITraceFeignPort traceFeignPort;

    public OrderSaveUseCase(IDishPersistencePort dishPersistencePort,
                            IOrderPersistencePort orderPersistencePort,
                            IOrderDishPersistencePort orderDishPersistencePort,
                            IUserFeignPort userFeignPort,
                            IUserSessionPort userSessionPort,
                            ITraceFeignPort traceFeignPort) {
        this.dishPersistencePort = dishPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.userFeignPort = userFeignPort;
        this.userSessionPort = userSessionPort;
        this.traceFeignPort = traceFeignPort;
    }

    @Override
    public void saveOrder(OrderReq orderReq) {
        ValidateRequest.checkId(orderReq.getIdRestaurant());
        List<OrderDish> orderDishList = orderReq.getOrderDishList();
        if (orderDishList == null || orderDishList.isEmpty()) {
            throw new BadRequestException();
        }

        User userClient = userFeignPort.getUserByEmail(userSessionPort.getCurrentUserEmail()).orElseThrow(UserNotExistsException::new);
        Long idClient = userClient.getId();
        if (orderPersistencePort.existsByIdClientAndStatusList(idClient, List.of(EN_PREPARACION, PENDIENTE, LISTO))) {
            throw new OrdenByIdClientExistsException();
        }

        List<Long> dishIds = orderDishList.stream()
                .map(OrderDish::getIdDish)
                .toList();

        Long countValidDishes = dishPersistencePort.countValidDishesByRestaurant(dishIds, orderReq.getIdRestaurant());

        if (countValidDishes != dishIds.size()) {
            throw new DishNotRestaurantException();
        }

        Order order = new Order();
        order.setIdClient(idClient);
        order.setDate(DateHelper.dateBogota());
        order.setStatus(PENDIENTE);
        order.setIdRestaurant(orderReq.getIdRestaurant());

        Long orderId = orderPersistencePort.saveOrder(order);
        orderDishList.forEach(orderDish -> orderDish.setIdOrder(orderId));

        orderDishPersistencePort.saveAllOrderDish(orderDishList);
        order.setId(orderId);

        Traceability traceability = TraceabilityHelper.createTrace(order, null, null);
        traceFeignPort.saveTraceability(traceability);
    }

}
