package com.hexagonal.ms_foodcourt.infrastructure.configuration;

import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import com.hexagonal.ms_foodcourt.domain.api.IOrderServicePort;
import com.hexagonal.ms_foodcourt.domain.api.IRestaurantServicePort;
import com.hexagonal.ms_foodcourt.domain.spi.ICategoryPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IOrderPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.domain.usecase.DishUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.OrderUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.RestaurantUseCase;
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
import com.hexagonal.ms_foodcourt.infrastructure.security.adapter.UserSessionAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), userFeignPort());
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort(), restaurantPersistencePort(), userFeignPort(), userSessionPort(), categoryPersistencePort());
    }

    @Bean
    public IOrderServicePort orderServicePort() {
        return new OrderUseCase(dishPersistencePort(), orderPersistencePort(), orderDishPersistencePort(), userFeignPort(), userSessionPort());
    }

}
