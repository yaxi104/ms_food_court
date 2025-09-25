package com.hexagonal.ms_foodcourt.domain.spi;

import com.hexagonal.ms_foodcourt.domain.model.OrderReadyEvent;

public interface ISqsSenderServicePort {

    void sendMessage(OrderReadyEvent orderReadyEvent);
}
