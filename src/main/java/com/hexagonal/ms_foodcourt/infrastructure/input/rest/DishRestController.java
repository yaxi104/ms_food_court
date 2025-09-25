package com.hexagonal.ms_foodcourt.infrastructure.input.rest;


import com.hexagonal.ms_foodcourt.application.dto.request.DishRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishToggleStatusRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.DishUpdateRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.DishResponse;
import com.hexagonal.ms_foodcourt.application.handler.IDishHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dish")
@RequiredArgsConstructor
@Tag(name = "Dish", description = "Operations related to dish the menu")
public class DishRestController {

    private final IDishHandler dishHandler;

    @Operation(
            summary = "Create a dish",
            description = "Creates a new dish. Only accessible by PROPIETARIO.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dish created"),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Bad Request Example",
                                            value = """
                                                    {
                                                        "Message": "The request contains invalid data. Please check the submitted fields and try again"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Not access",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Forbidden Example",
                                            value = """
                                                    {
                                                        "Message": "You do not have permission to access this resource"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409", description = "Conflict",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Conflict Example",
                                            value = """
                                                    {
                                                        "Message": "Dish already exists"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/owner")
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<Void> saveDish(@Valid @RequestBody DishRequest dishRequest) {
        dishHandler.saveDish(dishRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Update a dish",
            description = "Update a dish. Only accessible by PROPIETARIO.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Dish updated"),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Bad Request Example",
                                            value = """
                                                    {
                                                        "Message": "The request contains invalid data. Please check the submitted fields and try again"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Not access",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Forbidden Example",
                                            value = """
                                                    {
                                                        "Message": "You do not have permission to access this resource"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409", description = "Conflict",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Conflict Example",
                                            value = """
                                                    {
                                                        "Message": "No data found for the requested petition"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/owner")
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<Void> updateDish(@Valid @RequestBody DishUpdateRequest dishUpdateRequest) {
        dishHandler.updateDish(dishUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Enable or disable a dish",
            description = "Allows the restaurant owner to enable or disable a dish from their own restaurant's menu.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Dish status updated successfully"),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Bad Request Example",
                                            value = """
                                                    {
                                                        "Message": "The request contains invalid data. Please check the submitted fields and try again"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Not access",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Forbidden Example",
                                            value = """
                                                    {
                                                        "Message": "You do not have permission to access this resource"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409", description = "Conflict",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Conflict Example",
                                            value = """
                                                    {
                                                        "Message": "User already exists"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PatchMapping("/toggle/status")
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<Void> toggleDishStatus(@RequestBody DishToggleStatusRequest dishToggleStatusRequest) {
        dishHandler.toggleDishStatus(dishToggleStatusRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get List of dish orden by precio",
            description = "Returns a paginated list of all dish, sorted by price. Accessible by users with CLIENTE.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pageable dish",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Dish Response Example",
                                            value = """
                                                    {
                                                         "content": [
                                                             {
                                                                 "id": 2,
                                                                 "name": "La Burguer max",
                                                                 "price": 60000,
                                                                 "description": "Hamburguesa triple carne, pollo desmechado, tocineta, cebolla caramelizada max triple",
                                                                 "imageUrl": "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&psig=AOvVaw2g9G0Sld15LB9VVzwp1f5-&ust=1758314236215000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCICW7YyV448DFQAAAAAdAAAAABAE",
                                                                 "categoryId": 1,
                                                                 "restaurantId": 2,
                                                                 "active": "true"
                                                             },
                                                             {
                                                                 "id": 3,
                                                                 "name": "La Burguer vegan",
                                                                 "price": 80000,
                                                                 "description": "Hamburguesa triple carne, pollo desmechado, tocineta, cebolla caramelizada max triple max vegan",
                                                                 "imageUrl": "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&psig=AOvVaw2g9G0Sld15LB9VVzwp1f5-&ust=1758314236215000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCICW7YyV448DFQAAAAAdAAAAABAE",
                                                                 "categoryId": 3,
                                                                 "restaurantId": 2,
                                                                 "active": "true"
                                                             }
                                                         ],
                                                         "totalPages": 1,
                                                         "totalElements": 2,
                                                         "last": true
                                                     }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Bad Request Example",
                                            value = """
                                                    {
                                                        "Message": "The request contains invalid data. Please check the submitted fields and try again"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403", description = "Not access",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Fordibben Example",
                                            value = """
                                                    {
                                                        "Message": "You do not have permission to access this resource"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "Not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found Example",
                                            value = """
                                                    {
                                                        "Message": "User not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PreAuthorize("hasAnyRole('CLIENTE')")
    @GetMapping("/all/{restaurantId}")
    public ResponseEntity<PaginatedResponse<DishResponse>> listRestaurants(@PathVariable Long restaurantId,
                                                                           @RequestParam(required = false) Long categoryId,
                                                                           @RequestParam(required = false) Integer page,
                                                                           @RequestParam(required = false) Integer size) {
        PaginatedResponse<DishResponse> dishResponsePage = dishHandler.getListDishes
                (restaurantId, categoryId, page, size);
        return ResponseEntity.ok(dishResponsePage);
    }
}
