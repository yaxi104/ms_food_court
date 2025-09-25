package com.hexagonal.ms_foodcourt.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {

    @Schema(description = "Id de la categoria", example = "1")
    private Long id;

    @Schema(description = "Nombre de la categoria", example = "Saludable")
    private String name;

    @Schema(description = "Descripcion de la categoria", example = "Comida Saludable")
    private String description;

}
