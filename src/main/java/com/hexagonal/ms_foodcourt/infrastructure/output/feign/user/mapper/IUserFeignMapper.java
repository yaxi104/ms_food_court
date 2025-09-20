package com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.mapper;

import com.hexagonal.ms_foodcourt.domain.model.request.User;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.model.UserFeign;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserFeignMapper {

    UserFeign toFeign(User user);

    User toUser(UserFeign userFeign);

}
