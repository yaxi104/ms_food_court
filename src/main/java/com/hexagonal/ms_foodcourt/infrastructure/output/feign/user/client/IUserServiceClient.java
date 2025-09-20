package com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.client;

import com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.model.UserFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${userservice.url}")
public interface IUserServiceClient {

    @GetMapping("/user")
    UserFeign getUserByEmail(@RequestParam("email") String email);

    @GetMapping("/{id}")
    UserFeign getUserById(@PathVariable("id") Long id);
}