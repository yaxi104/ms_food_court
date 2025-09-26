package com.hexagonal.ms_foodcourt.infrastructure.output.feign.trace.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TraceabilityFeign {

    private Long orderId;
    private Long clientId;
    private LocalDateTime date;
    private String previousStatus;
    private String newStatus;
    private Long employeeId;
    private String employeeEmail;
}
