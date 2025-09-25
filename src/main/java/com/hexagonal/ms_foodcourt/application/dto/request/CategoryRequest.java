package com.hexagonal.ms_foodcourt.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank
    @Schema(description = "Nombre de la categoria", example = "Saludable")
    private String name;

    @NotBlank
    @Schema(description = "Descripcion de la categoria", example = "Comida Saludable")
    private String description;

}
