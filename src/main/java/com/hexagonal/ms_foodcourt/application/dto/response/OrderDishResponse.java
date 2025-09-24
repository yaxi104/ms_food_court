package com.hexagonal.ms_foodcourt.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDishResponse {

    private Long idDish;
    private String nameDish;
    private Integer quantity;

}
