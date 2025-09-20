package com.hexagonal.ms_foodcourt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
//@EnableFeignClients(basePackages = "com.hexagonal.ms_foodcourt.infrastructure.output.feign.client")
public class MsFoodCourtApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsFoodCourtApplication.class, args);
    }

}
