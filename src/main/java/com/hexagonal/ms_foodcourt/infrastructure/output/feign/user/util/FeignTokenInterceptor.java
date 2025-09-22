package com.hexagonal.ms_foodcourt.infrastructure.output.feign.user.util;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignTokenInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        var attributes = RequestContextHolder.getRequestAttributes();

        if (attributes instanceof ServletRequestAttributes servletAttributes) {
            String token = servletAttributes.getRequest().getHeader("Authorization");
            if (token != null) {
                template.header("Authorization", token);
            }
        }
    }
}