package com.hexagonal.ms_foodcourt.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {

    private Long id;

    private Long idClient;

    private LocalDateTime date;

    private String status;

    private Long idRestaurant;

    private Long idChef;

    private List<OrderDishResponse> orderDishResponses;
}
