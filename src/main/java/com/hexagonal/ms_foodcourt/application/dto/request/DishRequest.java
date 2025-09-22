package com.hexagonal.ms_foodcourt.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequest {

    @NotBlank
    @Schema(description = "Nombre del plato", example = "Hamburguesa Clásica")
    private String name;

    @NotNull
    @Positive(message = "Valor no válido")
    @Schema(description = "Precio del plato en números enteros", example = "15000")
    private Integer price;

    @NotBlank
    @Schema(description = "Descripción del plato", example = "Jugosa hamburguesa con queso cheddar, lechuga y tomate.")
    private String description;

    @NotBlank(message = "Image URL is required")
    @Pattern(
            regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$",
            message = "Formato no válido"
    )
    @Schema(description = "URL de la imagen del plato", example = "https://miapp.com/imagenes/hamburguesa.jpg")
    private String imageUrl;

    @NotNull
    @Schema(description = "Categoría del plato", example = "1")
    private Long categoryId;

    @NotNull
    @Schema(description = "ID del restaurante al que pertenece el plato", example = "10")
    private Long restaurantId;
}
