package com.hexagonal.ms_foodcourt.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDishRequest {
    @NotNull
    @Schema(description = "Id del plato", example = "1")
    private Long idDish;

    @NotNull
    @Schema(description = "Cantidad", example = "2")
    private Integer quantity;

}
