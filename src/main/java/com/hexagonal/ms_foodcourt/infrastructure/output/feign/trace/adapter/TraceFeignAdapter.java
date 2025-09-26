package com.hexagonal.ms_foodcourt.infrastructure.output.feign.trace.adapter;

import com.hexagonal.ms_foodcourt.domain.model.Traceability;
import com.hexagonal.ms_foodcourt.domain.spi.ITraceFeignPort;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.trace.client.ITraceServiceClient;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.trace.mapper.ITraceFeignMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TraceFeignAdapter implements ITraceFeignPort {

    private final ITraceServiceClient traceServiceClient;

    private final ITraceFeignMapper traceFeignMapper;

    public TraceFeignAdapter(ITraceServiceClient traceServiceClient, ITraceFeignMapper traceFeignMapper) {
        this.traceServiceClient = traceServiceClient;
        this.traceFeignMapper = traceFeignMapper;
    }

    @Override
    public void saveTraceability(Traceability traceability) {
        try {
            traceServiceClient.saveTraceability(traceFeignMapper.toTraceabilityFeign(traceability));
        } catch (Exception e) {
            log.error("Error save traceability {}", e.getMessage());
        }
    }
}
