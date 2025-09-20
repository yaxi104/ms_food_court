package com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.util;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class FeignTokenInterceptor implements RequestInterceptor {

    private final HttpServletRequest request;

    public FeignTokenInterceptor(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void apply(RequestTemplate template) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            template.header("Authorization", token);
        }
    }
}
