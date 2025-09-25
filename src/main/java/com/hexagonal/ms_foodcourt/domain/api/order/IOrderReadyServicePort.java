package com.hexagonal.ms_foodcourt.domain.api.order;

public interface IOrderReadyServicePort {

    void markOrderAsReady(Long idOrder);
}
