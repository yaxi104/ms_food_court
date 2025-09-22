package com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.mapper;

import com.hexagonal.ms_foodcourt.domain.model.Role;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.model.RoleFeign;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRoleFeignMapper {

    RoleFeign toFeign(Role role);

    Role toRole(RoleFeign roleFeign);

}
