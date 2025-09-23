package com.hexagonal.ms_foodcourt.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    @NotNull
    @Schema(description = "ID del restaurante", example = "8")
    private Long idRestaurant;

    @NotNull
    @Schema(description = "Lista platos y cantidad")
    private List<OrderDishRequest> orderDishRequests;
}
