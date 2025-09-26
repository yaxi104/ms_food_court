package com.hexagonal.ms_foodcourt.infrastructure.output.feign.trace.mapper;

import com.hexagonal.ms_foodcourt.domain.model.Traceability;
import com.hexagonal.ms_foodcourt.infrastructure.output.feign.trace.model.TraceabilityFeign;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITraceFeignMapper {

    Traceability toTraceability(TraceabilityFeign traceabilityFeign);

    TraceabilityFeign toTraceabilityFeign(Traceability traceability);

}
