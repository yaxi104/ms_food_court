package com.hexagonal.ms_foodcourt.infrastructure.input.rest;


import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dish")
@RequiredArgsConstructor
@Tag(name = "Dish", description = "Operations related to dish the menu")
public class DishRestController {

    private final IDishHandler dishHandler;

    @Operation(
            summary = "Create dish the menu",
            description = "Creates a new dish. Only accessible by PROPIETARIO.",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dish created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "409", description = "Dish already exists")}
    )
    @PostMapping("/owner")
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<Void> saveDish(@Valid @RequestBody DishRequest dishRequest) {
        dishHandler.saveDish(dishRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Update dish the menu",
            description = "Update a dish. Only accessible by PROPIETARIO.",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dish created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request"),
                    @ApiResponse(responseCode = "409", description = "No data found for the requested petition")}
    )
    @PatchMapping("/owner")
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<Void> updateDish(@Valid @RequestBody DishUpdateRequest dishUpdateRequest) {
        dishHandler.updateDish(dishUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
