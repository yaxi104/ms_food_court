package com.hexagonal.ms_foodcourt.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishToggleStatusRequest {

    @NotNull
    @Schema(description = "Id del plato del menu", example = "1")
    private Long id;

    @NotNull
    @Schema(description = "Estado habilitado/deshabilitado", example = "1")
    private String active;

}
