package com.hexagonal.ms_foodcourt.infrastructure.input.rest;


import com.hexagonal.ms_foodcourt.application.dto.request.DeliverOrderRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.OrderRequest;
import com.hexagonal.ms_foodcourt.application.dto.request.PaginatedResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.MessageResponse;
import com.hexagonal.ms_foodcourt.application.dto.response.OrderResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Operation(
            summary = "Get orders by status",
            description = "Returns orders by status. Accessible by users with roles EMPLEADO.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Orders list",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Order List Response Example",
                                            value = """
                                                    {
                                                        "content": [
                                                            {
                                                                "id": 5,
                                                                "idClient": 4,
                                                                "date": "2025-09-24T23:04:18",
                                                                "status": "LISTO",
                                                                "idRestaurant": 2,
                                                                "idChef": null,
                                                                "orderDishResponses": [
                                                                    {
                                                                        "idDish": 2,
                                                                        "nameDish": "La Burguer max",
                                                                        "quantity": 3
                                                                    },
                                                                    {
                                                                        "idDish": 3,
                                                                        "nameDish": "La Burguer vegan",
                                                                        "quantity": 2
                                                                    }
                                                                ]
                                                            }
                                                        ],
                                                        "totalPages": 1,
                                                        "totalElements": 1,
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
                                                        "Message": "Role not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('EMPLEADO')")
    @GetMapping("/all")
    public ResponseEntity<PaginatedResponse<OrderResponse>> getAllOrderByStatus(@RequestParam String status,
                                                                                @RequestParam Long idRestaurant,
                                                                                @RequestParam(required = false) Integer page,
                                                                                @RequestParam(required = false) Integer size) {
        PaginatedResponse<OrderResponse> orderResponses = orderHandler.getAllOrderByStatus(status, idRestaurant, page, size);
        return ResponseEntity.ok(orderResponses);
    }

    @Operation(
            summary = "Assign order to authenticated employee and set status to 'In Preparation'",
            description = "Allows an authenticated employee to assign themselves to an order and update its status to 'In Preparation'. Only orders belonging to the employee's restaurant can be assigned.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Order successfully assigned and status updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Order assign Example",
                                            value = """
                                                    {
                                                        "message": "Order has been successfully assigned"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Forbidden",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Forbidden Example",
                                            value = """
                                                    {
                                                        "message": "You do not have permission to assign this order"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Order not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found Example",
                                            value = """
                                                    {
                                                        "Message": "Order not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('EMPLEADO')")
    @PostMapping("/assign/{orderId}")
    public ResponseEntity<MessageResponse> assignOrderToEmployee(@PathVariable Long orderId) {
        MessageResponse messageResponse = orderHandler.assignOrderToEmployee(orderId);
        return ResponseEntity.ok(messageResponse);
    }

    @Operation(
            summary = "Mark order as 'LISTO'",
            description = "Marks the order status as 'LISTO' and triggers notification to the client. Only valid if order exists.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order successfully marked as LISTO", content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Order Ready Example",
                                    value = """
                                                    {
                                                        "message": "Order has been successfully assigned"
                                                    }
                                            """
                            )
                    )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid request",
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
                    @ApiResponse(responseCode = "403", description = "Forbidden",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Forbidden Example",
                                            value = """
                                                    {
                                                        "Message": "You do not have permission to mark this order as ready"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Order not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found Example",
                                            value = """
                                                    {
                                                        "Message": "Order not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('EMPLEADO')")
    @PostMapping("/ready/{orderId}")
    public ResponseEntity<MessageResponse> markOrderAsReady(@PathVariable Long orderId) {
        MessageResponse messageResponse = orderHandler.markOrderAsReady(orderId);
        return ResponseEntity.ok(messageResponse);
    }

    @Operation(
            summary = "Mark order as 'ENTREGADO'",
            description = "Marks the order status as 'ENTREGADO' if the order is in 'LISTO' state and the correct PIN is provided. Only employees of the restaurant can perform this action.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order successfully marked as ENTREGADO",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Success Example",
                                            value = """
                                                    {
                                                        "message": "Your order has been delivered"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Forbidden",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Forbidden Example",
                                            value = """
                                                    {
                                                        "message": "You do not have permission to deliver this order"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Order not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found Example",
                                            value = """
                                                    {
                                                        "message": "Order not found"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Conflict - Invalid order status",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Conflict Example",
                                            value = """
                                                    {
                                                        "message": "Only orders in 'LISTO' state can be marked as delivered"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('EMPLEADO')")
    @PostMapping("/delivered")
    public ResponseEntity<MessageResponse> markOrderAsDelivered(@RequestBody DeliverOrderRequest deliverOrderRequest) {
        MessageResponse messageResponse = orderHandler.markOrderAsDelivered(deliverOrderRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @Operation(
            summary = "Mark order as 'CANCELADO'",
            description = "Marks the order status as 'CANCELADO' if the order is in 'PENDIENTE' state. Only customer can perform this action.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order successfully marked as CANCELADO",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Success Example",
                                            value = """
                                                    {
                                                        "message": "Your order has been canceled"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "403", description = "Forbidden",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Forbidden Example",
                                            value = """
                                                    {
                                                        "message": "You do not have permission to deliver this order"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Order not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Not Found Example",
                                            value = """
                                                    {
                                                        "message": "Order not found"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "409", description = "Conflict - Invalid order status",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Conflict Example",
                                            value = """
                                                    {
                                                        "message": "Only orders in 'LISTO' state can be marked as delivered"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/canceled/{orderId}")
    public ResponseEntity<MessageResponse> markOrderAsCanceled(@PathVariable Long orderId) {
        MessageResponse messageResponse = orderHandler.markOrderAsCanceled(orderId);
        return ResponseEntity.ok(messageResponse);
    }
}
