package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.Traceability;

public interface ITraceFeignPort {
    void saveTraceability(Traceability traceability);
}
