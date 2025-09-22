package com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Role;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.client.IRoleServiceClient;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.mapper.IRoleFeignMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.model.RoleFeign;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleFeignAdapterTest {

    @Mock
    private IRoleServiceClient roleServiceClient;

    @Mock
    private IRoleFeignMapper roleFeignMapper;

    private RoleFeignAdapter roleFeignAdapter;

    @BeforeEach
    void setUp() {
        roleFeignAdapter = new RoleFeignAdapter(roleServiceClient, roleFeignMapper);
    }

    @Test
    void getRoleByName_ShouldReturnRole_WhenFound() {
        String roleName = "ADMIN";
        RoleFeign feignRole = new RoleFeign(1L, "ADMIN", "Administrador");
        Role expectedRole = new Role(1L, "ADMIN", "Administrador");

        when(roleServiceClient.getRoleByName(roleName)).thenReturn(feignRole);
        when(roleFeignMapper.toRole(feignRole)).thenReturn(expectedRole);

        Optional<Role> result = roleFeignAdapter.getRoleByName(roleName);

        assertTrue(result.isPresent());
        assertEquals(expectedRole, result.get());
        verify(roleServiceClient).getRoleByName(roleName);
        verify(roleFeignMapper).toRole(feignRole);
    }

    @Test
    void getRoleByName_ShouldReturnEmpty_WhenNotFound() {
        String roleName = "NOT_EXIST";
        when(roleServiceClient.getRoleByName(roleName))
                .thenThrow(FeignException.NotFound.class);

        Optional<Role> result = roleFeignAdapter.getRoleByName(roleName);

        assertTrue(result.isEmpty());
        verify(roleServiceClient).getRoleByName(roleName);
        verify(roleFeignMapper, never()).toRole(any());
    }

    @Test
    void getRoleById_ShouldReturnRole_WhenFound() {
        Long roleId = 1L;
        RoleFeign feignRole = new RoleFeign(1L, "ADMIN", "Administrador");
        Role expectedRole = new Role(1L, "ADMIN", "Administrador");

        when(roleServiceClient.getRoleById(roleId)).thenReturn(feignRole);
        when(roleFeignMapper.toRole(feignRole)).thenReturn(expectedRole);

        Optional<Role> result = roleFeignAdapter.getRoleById(roleId);

        assertTrue(result.isPresent());
        assertEquals(expectedRole, result.get());
        verify(roleServiceClient).getRoleById(roleId);
        verify(roleFeignMapper).toRole(feignRole);
    }

    @Test
    void getRoleById_ShouldReturnEmpty_WhenNotFound() {
        Long roleId = 99L;
        when(roleServiceClient.getRoleById(roleId))
                .thenThrow(FeignException.NotFound.class);

        Optional<Role> result = roleFeignAdapter.getRoleById(roleId);

        assertTrue(result.isEmpty());
        verify(roleServiceClient).getRoleById(roleId);
        verify(roleFeignMapper, never()).toRole(any());
    }
}