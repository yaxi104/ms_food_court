package com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.adapter;

import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.model.UserAuth;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.client.IUserServiceClient;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.mapper.IUserFeignMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.model.UserAuthFeign;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.model.UserFeign;
import com.hexagonal.ms_foodcourt.util.TestDataRestaurantFactory;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class UserFeignAdapterTest {

    @Mock
    private IUserServiceClient userServiceClient;

    @Mock
    private IUserFeignMapper userFeignMapper;

    private UserFeignAdapter userFeignAdapter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userFeignAdapter = new UserFeignAdapter(userServiceClient, userFeignMapper);
    }

    @Test
    void getUserByEmailShouldReturnUserWhenUserExists() {
        String email = "test@example.com";
        UserFeign userFeign = TestDataRestaurantFactory.mockUserFeign();
        User userMock = TestDataRestaurantFactory.mockUser();

        when(userServiceClient.getUserByEmail(email)).thenReturn(userFeign);
        when(userFeignMapper.toUser(userFeign)).thenReturn(userMock);

        Optional<User> result = userFeignAdapter.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(userMock, result.get());

        verify(userServiceClient).getUserByEmail(email);
        verify(userFeignMapper).toUser(userFeign);
    }

    @Test
    void getUserByEmailShouldReturnEmptyWhenUserNotFound() {
        String email = "notfound@example.com";

        when(userServiceClient.getUserByEmail(email)).thenThrow(FeignException.NotFound.class);

        Optional<User> result = userFeignAdapter.getUserByEmail(email);

        assertTrue(result.isEmpty());

        verify(userServiceClient).getUserByEmail(email);
        verifyNoInteractions(userFeignMapper);
    }

    @Test
    void getUserByIdShouldReturnUserWhenUserExists() {
        Long id = 123L;
        UserFeign userFeign = TestDataRestaurantFactory.mockUserFeign();
        User userMock = TestDataRestaurantFactory.mockUser();

        when(userServiceClient.getUserById(id)).thenReturn(userFeign);
        when(userFeignMapper.toUser(userFeign)).thenReturn(userMock);

        Optional<User> result = userFeignAdapter.getUserByid(id);

        assertTrue(result.isPresent());
        assertEquals(userMock, result.get());

        verify(userServiceClient).getUserById(id);
        verify(userFeignMapper).toUser(userFeign);
    }

    @Test
    void getUserByIdShouldReturnEmptyWhenUserNotFound() {
        Long id = 999L;

        when(userServiceClient.getUserById(id)).thenThrow(FeignException.NotFound.class);

        Optional<User> result = userFeignAdapter.getUserByid(id);

        assertTrue(result.isEmpty());

        verify(userServiceClient).getUserById(id);
        verifyNoInteractions(userFeignMapper);
    }


    @Test
    void getUserByIdAuthShouldReturnUserWhenFound() {
        Long userId = 1L;
        UserAuthFeign feignUser = new UserAuthFeign(1L, "John", "john@email.com", "USER");
        UserAuth expectedUser = new UserAuth(1L, "John", "john@email.com", "USER");

        when(userServiceClient.getUserByIdAuth(userId)).thenReturn(feignUser);
        when(userFeignMapper.toUserAuth(feignUser)).thenReturn(expectedUser);

        Optional<UserAuth> result = userFeignAdapter.getUserByIdAuth(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        verify(userServiceClient).getUserByIdAuth(userId);
        verify(userFeignMapper).toUserAuth(feignUser);
    }

    @Test
    void getUserByIdAuthShouldReturnEmptyWhenNotFound() {
        Long userId = 99L;
        when(userServiceClient.getUserByIdAuth(userId))
                .thenThrow(FeignException.NotFound.class);

        Optional<UserAuth> result = userFeignAdapter.getUserByIdAuth(userId);

        assertTrue(result.isEmpty());
        verify(userServiceClient).getUserByIdAuth(userId);
        verify(userFeignMapper, never()).toUserAuth(any());
    }
}
