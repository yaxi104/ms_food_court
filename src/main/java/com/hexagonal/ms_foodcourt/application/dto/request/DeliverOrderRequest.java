package com.hexagonal.ms_foodcourt.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliverOrderRequest {

    @NotNull
    @Schema(description = "Id del pedido", example = "1")
    private Long orderId;

    @NotBlank
    @Schema(description = "Pin entrega pedido", example = "123456")
    private String pin;
}
