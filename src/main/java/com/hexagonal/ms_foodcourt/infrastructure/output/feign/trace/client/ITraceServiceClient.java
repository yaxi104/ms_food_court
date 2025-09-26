package com.hexagonal.ms_foodcourt.infrastructure.output.feign.trace.client;

import com.hexagonal.ms_foodcourt.infrastructure.output.feign.trace.model.TraceabilityFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "trace-service", url = "${traceservice.url}")
public interface ITraceServiceClient {

    @PostMapping(value = "/trace", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveTraceability(@RequestBody TraceabilityFeign traceabilityFeign);

}