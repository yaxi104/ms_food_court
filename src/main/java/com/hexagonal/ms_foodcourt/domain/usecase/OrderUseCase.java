package com.hexagonal.ms_foodcourt.domain.usecase;

import com.hexagonal.ms_foodcourt.domain.api.IOrderServicePort;
import com.hexagonal.ms_foodcourt.domain.exception.BadRequestException;
import com.hexagonal.ms_foodcourt.domain.exception.DishNotRestaurantException;
import com.hexagonal.ms_foodcourt.domain.exception.OrdenByIdClientExistsException;
import com.hexagonal.ms_foodcourt.domain.exception.UserForbiddenException;
import com.hexagonal.ms_foodcourt.domain.exception.UserNotExistsException;
import com.hexagonal.ms_foodcourt.domain.model.Order;
import com.hexagonal.ms_foodcourt.domain.model.OrderDish;
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
import com.hexagonal.ms_foodcourt.domain.utils.DateHelper;
import com.hexagonal.ms_foodcourt.domain.utils.PageableHelper;
import com.hexagonal.ms_foodcourt.domain.utils.ValidateRequest;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.EN_PREPARACION;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.LISTO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.PENDIENTE;

public class OrderUseCase implements IOrderServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IUserFeignPort userFeignPort;
    private final IUserSessionPort userSessionPort;

    public OrderUseCase(IDishPersistencePort dishPersistencePort,
                        IOrderPersistencePort orderPersistencePort,
                        IOrderDishPersistencePort orderDishPersistencePort,
                        IUserFeignPort userFeignPort,
                        IUserSessionPort userSessionPort) {
        this.dishPersistencePort = dishPersistencePort;
        this.orderPersistencePort = orderPersistencePort;
        this.orderDishPersistencePort = orderDishPersistencePort;
        this.userFeignPort = userFeignPort;
        this.userSessionPort = userSessionPort;
    }


    @Override
    public void saveOrder(OrderReq orderReq) {
        Long idClient = orderReq.getIdClient();
        ValidateRequest.checkId(idClient);
        ValidateRequest.checkId(orderReq.getIdRestaurant());
        List<OrderDish> orderDishList = orderReq.getOrderDishList();
        if (orderDishList == null || orderDishList.isEmpty()) {
            throw new BadRequestException();
        }

        User userClient = userFeignPort.getUserByEmail(userSessionPort.getCurrentUserEmail()).orElseThrow(UserNotExistsException::new);

        if (!Objects.equals(userClient.getId(), idClient)) {
            throw new UserForbiddenException();
        }
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
    }

    @Override
    public PageResult<OrderResult> getAllOrderByStatus(String status, Long idRestaurant, Integer page, Integer size) {
        ValidateRequest.checkStatusOrderValid(status);
        ValidateRequest.checkId(idRestaurant);
        User userEmployee = userFeignPort.getUserByEmail(userSessionPort.getCurrentUserEmail()).orElseThrow(UserNotExistsException::new);
        if (!Objects.equals(userEmployee.getRestaurantId(), idRestaurant)) {
            throw new UserForbiddenException();
        }

        PageInfo pageInfo = PageableHelper.getPageable(page, size, "date");
        PageResult<OrderResult> orderPage = orderPersistencePort.findByStatusAndIdRestaurant(status, idRestaurant, pageInfo);
        List<OrderResult> orderList = orderPage.getContent();
        if (orderList.isEmpty()) {
            return new PageResult<>(Collections.emptyList(), 0, 0, false);
        }

        List<OrderResult> orderResults = orderList.stream().map(this::getOrderResult).toList();
        orderPage.setContent(orderResults);
        return orderPage;
    }

    private OrderResult getOrderResult(OrderResult orderResult) {
        List<OrderDishResult> orderDishResults = orderDishPersistencePort.findAllByIdOrderWithNames(orderResult.getId());
        orderResult.setOrderDishResponses(orderDishResults);
        return orderResult;
    }
}
