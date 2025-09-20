package com.hexagonal.ms_foodcourt.infrastructure.output.jpa.user.adapter;

import com.hexagonal.ms_foodcourt.domain.model.request.User;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.user.mapper.IUserEntityMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.jpa.user.repository.IUserRepository;
import com.hexagonal.ms_foodcourt.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserJpaAdapterTest {

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IUserEntityMapper userEntityMapper;

    @Test
    void findByEmailWhenUserExistsTest() {
        var email = "test@example.com";
        var mockEntity = TestDataFactory.mockUserEntity();
        var expectedUser = TestDataFactory.mockUser();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockEntity));
        when(userEntityMapper.toUser(mockEntity)).thenReturn(expectedUser);

        Optional<User> result = userJpaAdapter.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
        verify(userRepository).findByEmail(email);
        verify(userEntityMapper).toUser(mockEntity);
    }

    @Test
    void findByEmailWhenUserDoesNotExistTest() {
        var email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userJpaAdapter.findByEmail(email);

        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(email);
        verify(userEntityMapper, never()).toUser(any());
    }

    @Test
    void existsByIdAndRoleWhenExistsTest() {
        Long userId = 1L;
        String role = "PROPIETARIO";

        when(userRepository.existsByIdAndRole(userId, role)).thenReturn(true);

        assertTrue(userJpaAdapter.existsByIdAndRole(userId, role));
        verify(userRepository).existsByIdAndRole(userId, role);
    }

    @Test
    void existsByIdAndRoleWhenNotExistsTest() {
        Long userId = 1L;
        String role = "PROPIETARIO";

        when(userRepository.existsByIdAndRole(userId, role)).thenReturn(false);

        assertFalse(userJpaAdapter.existsByIdAndRole(userId, role));
        verify(userRepository).existsByIdAndRole(userId, role);
    }

}

