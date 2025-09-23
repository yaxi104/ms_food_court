package com.hexagonal.ms_foodcourt.infrastructure.input.rest;


import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.application.handler.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Operations related to order")
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @Operation(
            summary = "Create a order",
            description = "Creates a new order. Only accessible by CLIENTE.",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order created"),
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
    @PostMapping("/customer")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> saveDish(@RequestBody OrderRequest orderRequest) {
        orderHandler.saveOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
