package com.hexagonal.ms_foodcourt.infrastructure.output.feign.role.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleFeign {

    private Long id;

    private String name;

    private String description;

}
