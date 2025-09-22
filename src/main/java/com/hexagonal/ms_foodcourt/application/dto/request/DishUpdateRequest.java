package com.hexagonal.ms_foodcourt.application.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties()
public class DishUpdateRequest {

    @NotNull
    @Schema(description = "Id del plato a actualizar", example = "1")
    private Long id;

    @Positive(message = "Valor no válido")
    @Schema(description = "Precio del plato en números enteros", example = "15000")
    private Integer price;

    @Schema(description = "Descripción del plato", example = "Jugosa hamburguesa con queso cheddar, lechuga y tomate.")
    private String description;

}
