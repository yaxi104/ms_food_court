package com.hexagonal.ms_foodcourt.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexagonal.ms_foodcourt.domain.api.ICategoryServicePort;
import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import com.hexagonal.ms_foodcourt.domain.api.IRestaurantServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderAssignServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderCanceledServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderDeliveredServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderGetListServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderReadyServicePort;
import com.hexagonal.ms_foodcourt.domain.api.order.IOrderSaveServicePort;
import com.hexagonal.ms_foodcourt.domain.spi.ICategoryPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IPinSecurityPort;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.ISqsSenderServicePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.domain.usecase.CategoryUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.DishUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.RestaurantUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.order.OrderAssignUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.order.OrderCanceledUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.order.OrderDeliveredUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.order.OrderGetListUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.order.OrderReadyUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.order.OrderSaveUseCase;
import com.hexagonal.ms_foodcourt.infrastructure.configuration.aws.AwsProperties;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.adapter.UserFeignAdapter;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.client.IUserServiceClient;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.mapper.IUserFeignMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.adapter.CategoryJpaAdapter;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.mapper.ICategoryEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.category.repository.ICategoryRepository;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.adapter.DishJpaAdapter;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.mapper.IDishEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.repository.IDishRepository;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.adapter.OrderJpaAdapter;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.mapper.IOrderEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.order.repository.IOrderRepository;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.adapter.OrderDishJpaAdapter;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.mapper.IOrderDishEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.orderdish.repository.IOrderDishRepository;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.adapter.RestaurantJpaAdapter;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.mapper.IRestaurantEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.restaurant.repository.IRestaurantRepository;
import com.hexagonal.ms_foodcourt.infrastructure.output.sqs.SqsSenderServiceAdapter;
import com.hexagonal.ms_foodcourt.infrastructure.security.adapter.PinSecurityAdapter;
import com.hexagonal.ms_foodcourt.infrastructure.security.adapter.UserSessionAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IUserServiceClient userServiceClient;
    private final IUserFeignMapper userFeignMapper;
    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;
    private final IOrderDishRepository orderDishRepository;
    private final IOrderDishEntityMapper orderDishEntityMapper;
    private final SqsClient sqsClient;
    private final AwsProperties awsProperties;
    private final ObjectMapper objectMapper;
    private final ICategoryEntityMapper categoryMapper;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IUserFeignPort userFeignPort() {
        return new UserFeignAdapter(userServiceClient, userFeignMapper);
    }

    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(dishRepository, dishEntityMapper);
    }

    @Bean
    public IUserSessionPort userSessionPort() {
        return new UserSessionAdapter();
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort() {
        return new CategoryJpaAdapter(categoryRepository, categoryEntityMapper);
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort() {
        return new OrderJpaAdapter(orderRepository, orderEntityMapper);
    }

    @Bean
    public IOrderDishPersistencePort orderDishPersistencePort() {
        return new OrderDishJpaAdapter(orderDishRepository, orderDishEntityMapper);
    }

    @Bean
    public IPinSecurityPort pinSecurityPort() {
        return new PinSecurityAdapter();
    }

    @Bean
    public ISqsSenderServicePort sqsSenderServicePort() {
        return new SqsSenderServiceAdapter(sqsClient, objectMapper, awsProperties);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), userFeignPort());
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort(), restaurantPersistencePort(), userFeignPort(), userSessionPort(), categoryPersistencePort());
    }

    @Bean
    public IOrderSaveServicePort orderServicePort() {
        return new OrderSaveUseCase(dishPersistencePort(), orderPersistencePort(), orderDishPersistencePort(), userFeignPort(), userSessionPort());
    }

    @Bean
    public IOrderAssignServicePort orderAssignServicePort() {
        return new OrderAssignUseCase(orderPersistencePort(), userFeignPort(), userSessionPort());
    }

    @Bean
    public IOrderReadyServicePort orderReadyServicePort() {
        return new OrderReadyUseCase(orderPersistencePort(), userFeignPort(), userSessionPort(), pinSecurityPort(), sqsSenderServicePort());
    }

    @Bean
    public IOrderGetListServicePort orderGetListServicePort() {
        return new OrderGetListUseCase(orderPersistencePort(), orderDishPersistencePort(), userFeignPort(), userSessionPort());
    }

    @Bean
    public ICategoryServicePort categoryServicePort() {
        return new CategoryUseCase(categoryPersistencePort());
    }

    @Bean
    public IOrderDeliveredServicePort orderDeliveredServicePort() {
        return new OrderDeliveredUseCase(orderPersistencePort(), userFeignPort(), userSessionPort(), pinSecurityPort());
    }

    @Bean
    public IOrderCanceledServicePort orderCanceledServicePort() {
        return new OrderCanceledUseCase(orderPersistencePort(), userFeignPort(), userSessionPort());
    }
}
