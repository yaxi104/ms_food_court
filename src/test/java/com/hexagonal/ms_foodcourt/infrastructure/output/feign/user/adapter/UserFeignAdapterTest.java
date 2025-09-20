package com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.adapter;

import com.hexagonal.ms_foodcourt.domain.model.request.User;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.client.IUserServiceClient;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.mapper.IUserFeignMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.model.UserFeign;
import com.hexagonal.ms_foodcourt.util.TestDataFactory;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        UserFeign userFeign = TestDataFactory.mockUserFeign();
        User userMock = TestDataFactory.mockUser();

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
        UserFeign userFeign = TestDataFactory.mockUserFeign();
        User userMock = TestDataFactory.mockUser();

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
}
