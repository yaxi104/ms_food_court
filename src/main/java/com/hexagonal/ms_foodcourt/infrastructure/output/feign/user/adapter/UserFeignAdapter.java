package com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.adapter;

import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.model.UserAuth;
import com.hexagonal.ms_foodcourt.domain.usecase.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.client.IUserServiceClient;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.mapper.IUserFeignMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.model.UserAuthFeign;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.model.UserFeign;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFeignAdapter implements IUserFeignPort {

    private final IUserServiceClient userServiceClient;

    private final IUserFeignMapper userFeignMapper;

    public UserFeignAdapter(IUserServiceClient userServiceClient, IUserFeignMapper userFeignMapper) {
        this.userServiceClient = userServiceClient;
        this.userFeignMapper = userFeignMapper;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        try {
            UserFeign userFeign = userServiceClient.getUserByEmail(email);
            return Optional.of(userFeignMapper.toUser(userFeign));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserByid(Long id) {
        try {
            UserFeign userFeign = userServiceClient.getUserById(id);
            return Optional.of(userFeignMapper.toUser(userFeign));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserAuth> getUserByIdAuth(Long id) {
        try {
            UserAuthFeign userAuthFeign = userServiceClient.getUserByIdAuth(id);
            return Optional.of(userFeignMapper.toUserAuth(userAuthFeign));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

}
