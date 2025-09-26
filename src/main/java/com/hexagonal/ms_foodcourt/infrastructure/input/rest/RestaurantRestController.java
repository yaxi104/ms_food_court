package com.hexagonal.ms_foodcourt.infrastructure.input.rest;

import com.hexagonal.ms_foodcourt.application.dto.request.RestaurantRequest;
import com.hexagonal.ms_foodcourt.application.dto.response.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.RestaurantResponse;
import com.hexagonal.ms_foodcourt.application.handler.IRestaurantHandler;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
@Validated
@Tag(name = "Restaurant", description = "Operations related to restaurants")
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(
            summary = "Create restaurant",
            description = "Creates a new restaurant. Only accessible by ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Restaurant created"),
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
                                                        "Message": "Restaurant already exists"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> saveRestaurant(@Valid @RequestBody RestaurantRequest restaurantRequest) {
        restaurantHandler.saveRestaurant(restaurantRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get List of Restaurants orden by nombre",
            description = "Returns a paginated list of all restaurants, sorted alphabetically by name. Accessible by users with CLIENTE.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pageable restaurant",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "User Response Example",
                                            value = """
                                                    {
                                                         "content": [
                                                             {
                                                                 "id": 3,
                                                                 "name": "A la 1 a la 2 a la 3",
                                                                 "urlLogo": "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&psig=AOvVaw2g9G0Sld15LB9VVzwp1f5-&ust=1758314236215000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCICW7YyV448DFQAAAAAdAAAAABAE"
                                                             },
                                                             {
                                                                 "id": 1,
                                                                 "name": "La piña del pingüino 5",
                                                                 "urlLogo": "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&psig=AOvVaw2g9G0Sld15LB9VVzwp1f5-&ust=1758314236215000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCICW7YyV448DFQAAAAAdAAAAABAE"
                                                             },
                                                             {
                                                                 "id": 4,
                                                                 "name": "Zaza mesa",
                                                                 "urlLogo": "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&psig=AOvVaw2g9G0Sld15LB9VVzwp1f5-&ust=1758314236215000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCICW7YyV448DFQAAAAAdAAAAABAE"
                                                             },
                                                             {
                                                                 "id": 2,
                                                                 "name": "Zaza ya cuza ya cuza",
                                                                 "urlLogo": "https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.freepik.es%2Ffotos-vectores-gratis%2Flogo-design&psig=AOvVaw2g9G0Sld15LB9VVzwp1f5-&ust=1758314236215000&source=images&cd=vfe&opi=89978449&ved=0CBUQjRxqFwoTCICW7YyV448DFQAAAAAdAAAAABAE"
                                                             }
                                                         ],
                                                         "totalPages": 1,
                                                         "totalElements": 4,
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
    @GetMapping("/all")
    public ResponseEntity<PaginatedResponse<RestaurantResponse>> listRestaurants(@RequestParam(required = false) Integer page,
                                                                                 @RequestParam(required = false) Integer size) {
        PaginatedResponse<RestaurantResponse> listRestaurants = restaurantHandler.getListRestaurants(page, size);
        return ResponseEntity.ok(listRestaurants);
    }
}
