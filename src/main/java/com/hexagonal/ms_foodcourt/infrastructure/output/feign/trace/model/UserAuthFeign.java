package com.hexagonal.ms_foodcourt.infrastructure.output.feign.trace.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthFeign {

    private Long id;
    private String email;
    private String password;
    private String role;

}
