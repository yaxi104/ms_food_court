package com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Role;
import com.hexagonal.ms_foodcourt.domain.usecase.spi.IRoleFeignPort;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.client.IRoleServiceClient;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.mapper.IRoleFeignMapper;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.model.RoleFeign;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleFeignAdapter implements IRoleFeignPort {

    private final IRoleServiceClient roleServiceClient;

    private final IRoleFeignMapper roleFeignMapper;

    public RoleFeignAdapter(IRoleServiceClient roleServiceClient, IRoleFeignMapper roleFeignMapper) {
        this.roleServiceClient = roleServiceClient;
        this.roleFeignMapper = roleFeignMapper;
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        try {
            RoleFeign roleFeign = roleServiceClient.getRoleByName(name);
            return Optional.of(roleFeignMapper.toRole(roleFeign));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Role> getRoleById(Long id) {
        try {
            RoleFeign roleFeign = roleServiceClient.getRoleById(id);
            return Optional.of(roleFeignMapper.toRole(roleFeign));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        }
    }
}
