package com.hexagonal.ms_foodcourt.infrastructure.configuration;

import com.hexagonal.ms_foodcourt.domain.api.IDishServicePort;
import com.hexagonal.ms_foodcourt.domain.api.IRestaurantServicePort;
import com.hexagonal.ms_foodcourt.domain.usecase.DishUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.RestaurantUseCase;
import com.hexagonal.ms_foodcourt.domain.usecase.spi.IDishPersistencePort;
import com.hexagonal.ms_foodcourt.domain.usecase.spi.IRestaurantPersistencePort;
import com.hexagonal.ms_foodcourt.domain.usecase.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.domain.usecase.spi.IUserSessionPort;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.adapter.UserFeignAdapter;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.client.IUserServiceClient;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.mapper.IUserFeignMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.adapter.DishJpaAdapter;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.mapper.IDishEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.dish.repository.IDishRepository;
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
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), userFeignPort());
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort(), restaurantPersistencePort(), userFeignPort(), userSessionPort());
    }

}
