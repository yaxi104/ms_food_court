package com.hexagonal.ms_foodcourt.infrastructure.security;

import com.hexagonal.ms_foodcourt.domain.model.Role;
import com.hexagonal.ms_foodcourt.domain.model.User;
import com.hexagonal.ms_foodcourt.domain.spi.IRoleFeignPort;
import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import com.hexagonal.ms_foodcourt.infrastructure.security.adapter.UserDetailServiceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_PROPIETARIO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailServiceAdapterTest {

    private IUserFeignPort userFeignPort;
    private IRoleFeignPort roleFeignPort;

    private UserDetailServiceAdapter userDetailsService;

    @BeforeEach
    void setUp() {
        userFeignPort = mock(IUserFeignPort.class);
        roleFeignPort = mock(IRoleFeignPort.class);

        userDetailsService = new UserDetailServiceAdapter(userFeignPort, roleFeignPort);
    }

    @Test
    void loadUserByUsernameUserFoundTest() {
        var mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setRoleId(1L);

        when(userFeignPort.getUserByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(roleFeignPort.getRoleById(1L)).thenReturn(Optional.of(new Role(1L, ROLE_PROPIETARIO, "mock")));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PROPIETARIO")));
    }

    @Test
    void loadUserByUsernameUserNotFoundTest() {
        when(userFeignPort.getUserByEmail("noexiste@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("noexiste@example.com");
        });
    }

    @Test
    void loadUserByUsernameRoleNotFoundTest() {
        var mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        mockUser.setRoleId(1L);

        when(userFeignPort.getUserByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(roleFeignPort.getRoleById(1L)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("noexiste@example.com");
        });
    }
}