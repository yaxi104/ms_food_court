package com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.client;

import com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.model.RoleFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "role-service", url = "${userservice.url}")
public interface IRoleServiceClient {

    @GetMapping("/foodcourt")
    RoleFeign getRoleByName(@RequestParam("name") String name);

    @GetMapping("/foodcourt/{id}")
    RoleFeign getRoleById(@PathVariable("id") Long id);

}