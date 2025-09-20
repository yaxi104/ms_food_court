package com.hexagonal.ms_foodcourt.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantRequest {

    @NotBlank
    @Pattern(regexp = ".*[a-zA-Z]+.*", message = "Formato no válido")
    private String name;

    @NotBlank
    @Pattern(regexp = "\\d+", message = "Formato no válido")
    private String nit;

    @NotBlank
    private String address;

    @NotBlank
    @Size(max = 13)
    @Pattern(regexp = "^\\+?\\d{7,13}$", message = "Formato no válido")
    private String phoneNumber;

    @NotBlank
    @Pattern(regexp = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$", message = "Formato no válido")
    private String urlLogo;

    @NotNull
    private Long ownerId;

}
